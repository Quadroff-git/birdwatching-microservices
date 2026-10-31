# Birdwatching microservices

A WIP back-end REST-API for a birdwatching app built as a pet-project with Spring Boot microservices

## Technology stack:
- Java 17 + Spring Boot + Gradle
- PostgreSQL and H2 RDBMS for production and testing respectively
- RabbitMQ for asynchronous messaging between services (publisher already implemented, consumer coming Soon<sup>TM</sup>)

## Logging
Request correlation id propagation between `sighting-service` and `bird-service` is described in
[docs/correlation-id.md](docs/correlation-id.md).
User-action audit logging, including the separate audit file and retention policy, is described in
[docs/audit-logging.md](docs/audit-logging.md).
Performance timing for database queries and inter-service calls, including the production-only slow
operation filter, is described in [docs/performance-logging.md](docs/performance-logging.md).