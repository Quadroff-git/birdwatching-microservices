package org.pileka.sighting_service.logging;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class PerformanceMonitorTest {

    @Test
    void logsFastOperationAtInfo() {
        Logger logger = mock(Logger.class);
        PerformanceMonitor monitor = new PerformanceMonitor(logger, 1000);

        String result = monitor.measure("db.test", () -> {
            return "ok";
        });

        assertThat(result).isEqualTo("ok");
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(logger).info(message.capture());
        assertThat(message.getValue()).contains(
                "operation=db.test",
                "durationMs=",
                "startedAt=",
                "finishedAt=",
                "success=true"
        );
        verify(logger, never()).warn(anyString());
    }

    @Test
    void logsSlowOperationAtWarn() {
        Logger logger = mock(Logger.class);
        PerformanceMonitor monitor = new PerformanceMonitor(logger, 0);

        monitor.measure("db.slow", () -> {
            return "ok";
        });

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(logger).warn(message.capture());
        assertThat(message.getValue()).contains("operation=db.slow", "success=true");
        verify(logger, never()).info(anyString());
    }

    @Test
    void logsFailureAndRethrows() {
        Logger logger = mock(Logger.class);
        PerformanceMonitor monitor = new PerformanceMonitor(logger, 0);

        assertThatThrownBy(() -> monitor.measure("db.fail", () -> {
            throw new IllegalStateException("boom");
        })).isInstanceOf(IllegalStateException.class);

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(logger).warn(message.capture());
        assertThat(message.getValue()).contains("operation=db.fail", "success=false");
    }
}
