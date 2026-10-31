# Performance logging (task 3.3)

Database queries and inter-service HTTP calls are timed and logged with their start time, end time,
duration and outcome. The production profile keeps only operations that exceed a configurable
threshold in the main log.

## What is timed

| Service | Operation name | Wrapped call |
| --- | --- | --- |
| `bird-service` | `db.bird.save` | `BirdRepository.save` (create/update) |
| `bird-service` | `db.bird.findById` | `BirdRepository.findById` (get/update/delete) |
| `bird-service` | `db.bird.findBy` | specification-based page query |
| `bird-service` | `db.bird.delete` | `BirdRepository.delete` |
| `sighting-service` | `db.sighting.save` | `SightingRepository.save` (create/update) |
| `sighting-service` | `db.sighting.findById` | `SightingRepository.findById` |
| `sighting-service` | `db.sighting.findBy` | specification-based page query |
| `sighting-service` | `db.sighting.delete` | `SightingRepository.delete` |
| `sighting-service` | `http.<METHOD> <path>` | every outgoing `RestClient` call (bird/user services) |

The HTTP interceptor is added to every injected `RestClient.Builder` by `PerformanceConfig`, so the
existing bird and user clients are covered without changing their constructors.

## Log format

A single completion line contains the start time, end time and duration:

```
operation=db.bird.findById durationMs=12 startedAt=2026-10-31T17:48:12.345Z finishedAt=2026-10-31T17:48:12.357Z success=true
```

The main log pattern adds the service name, correlation id, thread and logger name, for example:

```
2026-10-31 20:48:12.357 INFO  [bird-service] [correlationId=demo-123] [http-nio-8081-exec-1] PERFORMANCE : operation=db.bird.findById durationMs=12 startedAt=... finishedAt=... success=true
```

Failures are recorded with `success=false` and then rethrown; existing exception handling and audit
logging are unchanged. Transport failures in the HTTP interceptor record `success=false` and rethrow
the original exception.

## Production filter

The `PERFORMANCE` logger is the filter:

| Profile | `logging.level.PERFORMANCE` | Main log contains |
| --- | --- | --- |
| default / dev | `INFO` | every timed operation |
| `prod` | `WARN` | only operations at or above the threshold |

- Fast operations are logged at `INFO`; operations at or above the threshold are logged at `WARN`.
- With the `prod` profile the logger level is `WARN`, so fast `INFO` performance lines are discarded
  before they reach the main appenders. Other application `INFO` logs are unaffected.
- The threshold is configured by `performance.slow-threshold-ms`
  (`PERFORMANCE_SLOW_THRESHOLD_MS`), default `1000` ms.

Configuration files:

- `application.yaml` — `logging.level.PERFORMANCE: INFO`, `performance.slow-threshold-ms`.
- `application-prod.yaml` — `logging.level.PERFORMANCE: WARN`.
- Performance lines go to the main log (`logs/application.log` and console); they do not go to the
  audit file.

## Demonstration

Default profile (all timings visible):

```bash
grep PERFORMANCE bird-service/logs/application.log
grep PERFORMANCE sighting-service/logs/application.log
```

Production profile with a forced threshold (every operation is treated as slow, so WARN lines are
visible; a real threshold of 1000 ms keeps only genuinely slow operations):

```bash
SPRING_PROFILES_ACTIVE=prod PERFORMANCE_SLOW_THRESHOLD_MS=0 \
  bash gradlew :bird-service:bootRun
```

With `SPRING_PROFILES_ACTIVE=prod` and no threshold override, fast operations are absent from the
main log and only slow `WARN` performance lines remain. Other `INFO` logs still appear.

## Files

| File | Purpose |
| --- | --- |
| `bird-service/.../logging/PerformanceMonitor.java` | Timing/formatting helper |
| `bird-service/.../logging/PerformanceConfig.java` | Creates the monitor bean |
| `sighting-service/.../logging/PerformanceMonitor.java` | Timing/formatting helper |
| `sighting-service/.../logging/PerformanceConfig.java` | Monitor bean + `RestClientCustomizer` |
| `sighting-service/.../logging/PerformanceClientHttpRequestInterceptor.java` | Times outgoing HTTP calls |
| `*/src/main/resources/application-prod.yaml` | Production performance logger level |

Tests:

- `bird-service/src/test/java/org/pileka/bird_service/logging/PerformanceMonitorTest.java`
- `sighting-service/src/test/java/org/pileka/sighting_service/logging/PerformanceMonitorTest.java`
- `sighting-service/src/test/java/org/pileka/sighting_service/logging/PerformanceClientHttpRequestInterceptorTest.java`
- `sighting-service/src/test/java/org/pileka/sighting_service/logging/PerformanceConfigTest.java`
- `sighting-service/src/test/java/org/pileka/sighting_service/logging/PerformanceFileTest.java`

## Limits

- Timings are per operation on the current thread; no metrics backend or percentiles are produced.
- The HTTP success flag means "the call completed without an exception", not "the response status was
  2xx". Business status handling remains in the clients.
- The threshold is a deployment setting, not a runtime-mutable value.
- Only DB calls and inter-service HTTP calls are timed; RabbitMQ publishing and controller execution
  are not.
- See [correlation-id.md](correlation-id.md) and [audit-logging.md](audit-logging.md) for the related
  logging features.
