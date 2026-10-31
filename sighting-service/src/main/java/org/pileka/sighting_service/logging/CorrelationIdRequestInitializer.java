package org.pileka.sighting_service.logging;

import org.slf4j.MDC;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.util.StringUtils;

/**
 * Copies the correlation id from the current thread's MDC into every outgoing HTTP request,
 * so that the callee service can continue the same trace.
 */
public class CorrelationIdRequestInitializer implements ClientHttpRequestInitializer {

    @Override
    public void initialize(ClientHttpRequest request) {
        String correlationId = MDC.get(CorrelationId.MDC_KEY);
        if (StringUtils.hasText(correlationId)) {
            request.getHeaders().set(CorrelationId.HEADER_NAME, correlationId);
        }
    }
}
