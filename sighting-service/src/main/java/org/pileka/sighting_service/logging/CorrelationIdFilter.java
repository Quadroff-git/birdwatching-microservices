package org.pileka.sighting_service.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Puts the correlation id of the current HTTP request into the SLF4J MDC, so that every log line
 * written while handling the request contains it. The id is taken from the incoming request header
 * or generated when the caller did not provide one.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String correlationId = resolveCorrelationId(request);
        MDC.put(CorrelationId.MDC_KEY, correlationId);
        response.setHeader(CorrelationId.HEADER_NAME, correlationId);

        try {
            log.info("Incoming request {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            log.info("Completed request {} {} with status {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus());
        } finally {
            MDC.remove(CorrelationId.MDC_KEY);
        }
    }

    private String resolveCorrelationId(HttpServletRequest request) {
        String headerValue = request.getHeader(CorrelationId.HEADER_NAME);
        return StringUtils.hasText(headerValue)
                ? headerValue.trim()
                : UUID.randomUUID().toString();
    }
}
