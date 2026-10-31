# Correlation ID propagation (`sighting-service` -> `bird-service`)

Implementation of module 3, task 3.1: every request can be traced across both services by a single
correlation id. The id is stored in the SLF4J **MDC** (Mapped Diagnostic Context) and printed by the
Logback pattern, so no log statement has to add it manually.

## What is MDC?

MDC is a thread-local key/value map managed by SLF4J and filled by `MDC.put(key, value)`. Logback's
`%X{key}` pattern converter reads that map when it renders a log event. Because the map is
thread-local, every log line produced on the request thread automatically gets the same value, and
no logger API changes are needed. The value must be removed when the request ends (`MDC.remove`),
otherwise it can leak to another request served by the same pooled thread.

In this project the key is `correlationId` and the header is `X-Correlation-Id`.

## How it works

1. **Incoming request** — `CorrelationIdFilter` (registered with highest precedence) reads
   `X-Correlation-Id` from the request. If the header is missing or blank it generates a UUID.
   It puts the value into MDC, echoes it in the response header, and logs the start and end of the
   request. The `finally` block removes only the `correlationId` MDC key.
2. **Outgoing request** — `sighting-service`'s `AbstractClientImpl` registers
   `CorrelationIdRequestInitializer` on the `RestClient` used for the bird/user clients. It copies
   the current MDC value into the outgoing `X-Correlation-Id` header. This runs on the same thread
   as the request, so it sees the MDC value set by the filter.
3. **Callee** — `bird-service`'s `CorrelationIdFilter` finds the header, puts it into its own MDC,
   and logs the handled request with the same id.
4. **Log output** — both `application.yaml` files define this console pattern:
   `... [correlationId=%X{correlationId:-}] ...`, so every line is greppable by id.

The gateway is not modified; Spring Cloud Gateway forwards unknown request headers by default.

## Files

| Service | File |
| --- | --- |
| `bird-service` | `src/main/java/org/pileka/bird_service/logging/CorrelationId.java` |
| `bird-service` | `src/main/java/org/pileka/bird_service/logging/CorrelationIdFilter.java` |
| `bird-service` | `src/main/resources/application.yaml` (`logging.pattern.console`) |
| `sighting-service` | `src/main/java/org/pileka/sighting_service/logging/CorrelationId.java` |
| `sighting-service` | `src/main/java/org/pileka/sighting_service/logging/CorrelationIdFilter.java` |
| `sighting-service` | `src/main/java/org/pileka/sighting_service/logging/CorrelationIdRequestInitializer.java` |
| `sighting-service` | `src/main/java/org/pileka/sighting_service/client/impl/AbstractClientImpl.java` |
| `sighting-service` | `src/main/resources/application.yaml` (`logging.pattern.console`) |

Tests:

- `bird-service/src/test/java/org/pileka/bird_service/logging/CorrelationIdFilterTest.java`
- `sighting-service/src/test/java/org/pileka/sighting_service/logging/CorrelationIdFilterTest.java`
- `sighting-service/src/test/java/org/pileka/sighting_service/client/impl/AbstractClientImplTest.java`

## Demonstration

Send a request with a known id (the request body requires an existing user and bird):

```bash
curl -X POST http://localhost:8080/api/sightings \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-Id: demo-123' \
  -d '{"birdId":1,"userId":1,"sightedAt":"2026-10-31T12:00:00","location":{"latitude":53.9,"longitude":27.56}}'
```

Then grep both services' logs:

```bash
docker compose logs sighting-service bird-service | grep demo-123
```

Expected path for this single id:

```
[sighting-service] ... Incoming request POST /api/sightings
[sighting-service] ... Creating sighting for birdId=1 and userId=1
[sighting-service] ... Checking whether bird with id=1 exists
[bird-service]     ... Incoming request GET /api/birds/1
[bird-service]     ... Fetching bird with id=1
[bird-service]     ... Completed request GET /api/birds/1 with status 200
[sighting-service] ... Sighting with id=... created
[sighting-service] ... Completed request POST /api/sightings with status 200
```

Without `X-Correlation-Id` the caller generates a UUID, returns it in the `X-Correlation-Id`
response header, and the same UUID appears in both services' logs.

## Tests

```bash
bash gradlew :bird-service:test --tests 'org.pileka.bird_service.logging.*'
bash gradlew :sighting-service:test --tests 'org.pileka.sighting_service.logging.*' --tests 'org.pileka.sighting_service.client.impl.*'
```

## Deliberate limits

- AMQP/RabbitMQ messages are not annotated with the id; the notification consumer is outside the
  scope of this task.
- Gateway and `user-service` logs are not instrumented. They still receive/forward the header, so
  they can be instrumented later without changing the wire contract.
- The correlation id is trusted as supplied by the caller. Only request-scoped MDC cleanup and
  blank/absent handling are implemented.
