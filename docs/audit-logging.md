# Audit logging (`sighting-service` and `bird-service`)

Implementation of module 3, task 3.2: user actions are written to a dedicated audit file through a
separate logger, with a retention policy different from the main debug log. Each audit line contains
the user, action, time, success flag, optional details, and the `correlationId` from task 3.1.

## What counts as "login" and "logout" here

`user-service` owns the real `/auth/login` endpoint, and there is no server-side session or logout
endpoint. `sighting-service` and `bird-service` are stateless JWT resource services, so their only
session boundary is the request itself:

- `LOGIN` — an authenticated request enters Spring MVC (a valid JWT has been accepted and the
  principal is known).
- `LOGOUT` — the request completes; success is `exception == null && HTTP status < 400`.

This is documented explicitly because real login/logout auditing would require changes in
`user-service`, which is outside this task's scope.

## Audited actions

| Action | Where | Details |
| --- | --- | --- |
| `LOGIN` | `AuditAccessInterceptor`, both services | request method + URI |
| `LOGOUT` | `AuditAccessInterceptor`, both services | request method + URI + response status |
| `BIRD_CREATE` | `bird-service`, `BirdServiceImpl` | bird id |
| `BIRD_UPDATE` | `bird-service`, `BirdServiceImpl` | bird id |
| `BIRD_DELETE` | `bird-service`, `BirdServiceImpl` | bird id |
| `SIGHTING_CREATE` | `sighting-service`, `SightingServiceImpl` | sighting id, bird id, user id |
| `SIGHTING_UPDATE` | `sighting-service`, `SightingServiceImpl` | sighting id |
| `SIGHTING_DELETE` | `sighting-service`, `SightingServiceImpl` | sighting id |

Data changes log `success=true` only after the service method has completed its work (including the
RabbitMQ publish for sighting creation). Any `RuntimeException` is audited with `success=false` and
`reason=<ExceptionSimpleName>`, then rethrown unchanged. Read-only requests are not audited as data
changes, but they still produce the `LOGIN`/`LOGOUT` request boundary entries.

## Audit line format

The `AUDIT` logger uses this file pattern:

```
%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [${appName}] [correlationId=%X{correlationId:-}] [%thread] %msg%n
```

Example:

```
2026-10-31 20:30:00.123 INFO  [sighting-service] [correlationId=demo-123] [http-nio-8083-exec-1] user=42 action=SIGHTING_CREATE success=true details=sightingId=10 birdId=1 userId=42
```

- **user** — JWT `sub` (the authenticated user id; an inter-service call uses the calling service
  name as its principal).
- **action** — one of the actions above.
- **time** — the timestamp rendered by the pattern.
- **success** — `true`/`false`; failures are logged at `WARN`.
- **details** — entity ids or request path/status.
- **correlationId** — inherited from the MDC set by the task 3.1 filter.

## Module

The audit logic is a reusable commons module: `birdwatching-commons/audit/lib`
(`birdwatching-commons:audit-lib`).

| File | Purpose |
| --- | --- |
| `audit/AuditLogger.java` | Writes to the `AUDIT` logger, resolves the user from the security context, formats the entry |
| `audit/AuditUserResolver.java` | Resolves the authenticated principal, skips anonymous requests |
| `audit/AuditAccessInterceptor.java` | Logs the `LOGIN`/`LOGOUT` request boundary |
| `config/AuditAutoConfiguration.java` | Registers the logger/interceptor and the MVC interceptor registration |
| `META-INF/spring/...AutoConfiguration.imports` | Enables the auto-configuration for services that depend on the module |

Both services declare `implementation project(':birdwatching-commons:audit-lib')`.

## Files and retention

Each service writes two files under its working directory:

| File | Logger | Rollover | Retention |
| --- | --- | --- | --- |
| `logs/application.log` | root logger | daily + 10 MB | 7 days / 100 MB total |
| `logs/audit.log` | `AUDIT` (additivity disabled) | daily + 10 MB | 90 days / 500 MB total |

Configuration:

- `logback-spring.xml` — includes Boot's `base.xml` (console + main file appenders) and adds the
  `AUDIT_FILE` `RollingFileAppender` with the audit logger.
- `application.yaml` — `logging.file.name`, `logging.pattern.file`, and
  `logging.logback.rollingpolicy.*` for the main log.
- `.gitignore` — `logs/` is runtime output and is not committed.

Audit entries never reach the console or `logs/application.log` because the `AUDIT` logger has
`additivity="false"` and only the `AUDIT_FILE` appender attached.

In Docker the `logs/` directory lives inside the container; mount a volume if the files must survive
container recreation (compose changes are outside this task's scope).

## Demonstration

Obtain a JWT from `user-service`, then create a bird or sighting:

```bash
curl -X POST http://localhost:8080/api/birds \
  -H 'Content-Type: application/json' \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"commonName":"Audit Falcon","scientificName":"Falco auditus","conservationStatus":"LEAST_CONCERN","averageWingspan":100.0,"averageWeight":900.0}'

tail -f bird-service/logs/audit.log
```

Expected entries for that request (same correlation id):

```
... INFO  [bird-service] [correlationId=...] ... user=42 action=LOGIN success=true details=request=POST /api/birds
... INFO  [bird-service] [correlationId=...] ... user=42 action=BIRD_CREATE success=true details=birdId=7
... INFO  [bird-service] [correlationId=...] ... user=42 action=LOGOUT success=true details=request=POST /api/birds status=200
```

A failed update/delete shows `success=false` and a `reason=...`, for example:

```
... WARN  [bird-service] [correlationId=...] ... user=42 action=BIRD_DELETE success=false details=birdId=999 reason=EntityDoesntExistException
```

## Tests

```bash
bash gradlew :birdwatching-commons:audit-lib:test
bash gradlew :bird-service:test --tests 'org.pileka.bird_service.service.*'
bash gradlew :sighting-service:test --tests 'org.pileka.sighting_service.service.*' --tests 'org.pileka.sighting_service.audit.*'
```

`AuditFileTest` in `sighting-service` asserts that an audit entry lands in `logs/audit.log` and not
in `logs/application.log`.

## Limits

- Real login/logout reside in `user-service` and are out of scope; the per-request mapping is the
  closest equivalent in a stateless JWT service.
- Authentication failures (401/403) are rejected before MVC, so they are not audit events; the main
  log's security handler still records them.
- Internal service calls are attributed to the calling service's JWT principal.
- Audit appender I/O errors are handled by Logback and never fail a request.
- See [correlation-id.md](correlation-id.md) for the correlation id that every audit line carries.
