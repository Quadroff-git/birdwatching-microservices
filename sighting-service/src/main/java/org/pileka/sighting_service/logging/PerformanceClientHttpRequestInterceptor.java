package org.pileka.sighting_service.logging;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.time.Instant;

/**
 * Times outgoing inter-service HTTP calls and records them through the shared {@link PerformanceMonitor}.
 */
public class PerformanceClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final PerformanceMonitor performanceMonitor;

    public PerformanceClientHttpRequestInterceptor(PerformanceMonitor performanceMonitor) {
        this.performanceMonitor = performanceMonitor;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        String operation = "http." + request.getMethod().name() + " " + request.getURI().getPath();
        long startNanos = System.nanoTime();
        Instant startedAt = Instant.now();

        try {
            ClientHttpResponse response = execution.execute(request, body);
            performanceMonitor.record(operation, startedAt, startNanos, true);
            return response;
        } catch (IOException | RuntimeException ex) {
            performanceMonitor.record(operation, startedAt, startNanos, false);
            throw ex;
        }
    }
}
