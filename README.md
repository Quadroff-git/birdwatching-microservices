# Birdwatching microservices

A WIP back-end REST-API for a birdwatching app built as a pet-project with Spring Boot microservices

## Technology stack:
- Java 17 + Spring Boot + Gradle
- PostgreSQL and H2 RDBMS for production and testing respectively
- RabbitMQ for asynchronous messaging between services (publisher already implemented, consumer coming Soon<sup>TM</sup>)

## Logging
Request correlation id propagation between `sighting-service` and `bird-service` is described in
[docs/correlation-id.md](docs/correlation-id.md).