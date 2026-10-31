package org.pileka.sighting_service.logging;

import org.slf4j.Logger;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Times an operation and logs its start/end timestamps, duration and outcome.
 * Fast operations are logged at INFO; operations at or above the threshold are logged at WARN,
 * so a production profile can set the PERFORMANCE logger level to WARN and keep only slow ones.
 */
public class PerformanceMonitor {

    public static final String LOGGER_NAME = "PERFORMANCE";

    private final Logger logger;

    private final long slowThresholdMs;

    public PerformanceMonitor(Logger logger, long slowThresholdMs) {
        this.logger = logger;
        this.slowThresholdMs = slowThresholdMs;
    }

    public <T> T measure(String operation, Supplier<T> action) {
        long startNanos = System.nanoTime();
        Instant startedAt = Instant.now();

        try {
            T result = action.get();
            record(operation, startedAt, startNanos, true);
            return result;
        } catch (RuntimeException ex) {
            record(operation, startedAt, startNanos, false);
            throw ex;
        }
    }

    public void measure(String operation, Runnable action) {
        measure(operation, () -> {
            action.run();
            return null;
        });
    }

    public void record(String operation, Instant startedAt, long startNanos, boolean success) {
        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
        Instant finishedAt = Instant.now();
        String message = "operation=%s durationMs=%d startedAt=%s finishedAt=%s success=%s"
                .formatted(operation, durationMs, startedAt, finishedAt, success);

        if (durationMs >= slowThresholdMs) {
            logger.warn(message);
        } else {
            logger.info(message);
        }
    }
}
