package org.pileka.sighting_service.client.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pileka.sighting_service.logging.CorrelationId;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.headerDoesNotExist;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AbstractClientImplTest {

    private RestClient.Builder builder;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
    }

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void propagatesCorrelationIdToOutgoingRequest() {
        MDC.put(CorrelationId.MDC_KEY, "correlation-id-42");
        AbstractClientImpl client = new AbstractClientImpl(builder, "http://localhost");

        server.expect(requestTo("http://localhost/42"))
                .andExpect(header(CorrelationId.HEADER_NAME, "correlation-id-42"))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        assertThat(client.resourceExists(42)).isTrue();
        server.verify();
    }

    @Test
    void omitsCorrelationIdWhenMdcIsEmpty() {
        AbstractClientImpl client = new AbstractClientImpl(builder, "http://localhost");

        server.expect(requestTo("http://localhost/42"))
                .andExpect(headerDoesNotExist(CorrelationId.HEADER_NAME))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        assertThat(client.resourceExists(42)).isTrue();
        server.verify();
    }
}
