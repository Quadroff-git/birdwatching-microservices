package org.pileka.sighting_service.logging;

/**
 * Shared names used to exchange the correlation id over HTTP and to store it in the logging context.
 */
public final class CorrelationId {

    public static final String HEADER_NAME = "X-Correlation-Id";

    public static final String MDC_KEY = "correlationId";

    private CorrelationId() {
    }
}
