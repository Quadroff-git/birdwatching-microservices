package org.pileka.sighting_service.logging;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PerformanceClientHttpRequestInterceptorTest {

    private final PerformanceMonitor performanceMonitor = mock(PerformanceMonitor.class);

    private final PerformanceClientHttpRequestInterceptor interceptor =
            new PerformanceClientHttpRequestInterceptor(performanceMonitor);

    @Test
    void recordsSuccessfulCall() throws Exception {
        HttpRequest request = mock(HttpRequest.class);
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getURI()).thenReturn(URI.create("http://localhost/api/birds/1"));
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        when(execution.execute(eq(request), any(byte[].class))).thenReturn(response);

        ClientHttpResponse result = interceptor.intercept(request, new byte[0], execution);

        assertThat(result).isSameAs(response);
        verify(performanceMonitor).record(
                eq("http.GET /api/birds/1"), any(Instant.class), anyLong(), eq(true)
        );
    }

    @Test
    void recordsFailedCallAndRethrows() throws Exception {
        HttpRequest request = mock(HttpRequest.class);
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getURI()).thenReturn(URI.create("http://localhost/api/birds/1"));
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        when(execution.execute(eq(request), any(byte[].class))).thenThrow(new IOException("boom"));

        assertThatThrownBy(() -> interceptor.intercept(request, new byte[0], execution))
                .isInstanceOf(IOException.class)
                .hasMessage("boom");

        verify(performanceMonitor).record(
                eq("http.GET /api/birds/1"), any(Instant.class), anyLong(), eq(false)
        );
    }
}
