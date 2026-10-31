package org.pileka.sighting_service.logging;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PerformanceConfigTest {

    @Test
    void customizerAddsPerformanceInterceptorToRestClient() {
        PerformanceMonitor performanceMonitor = mock(PerformanceMonitor.class);
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();

        new PerformanceConfig()
                .performanceRestClientCustomizer(performanceMonitor)
                .customize(builder);

        RestClient client = builder.baseUrl("http://localhost").build();
        server.expect(requestTo("http://localhost/1"))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        client.get().uri("/1").retrieve().toBodilessEntity();

        verify(performanceMonitor).record(
                eq("http.GET /1"), any(Instant.class), anyLong(), eq(true)
        );
    }
}
