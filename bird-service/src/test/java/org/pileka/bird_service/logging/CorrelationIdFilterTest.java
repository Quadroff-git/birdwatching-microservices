package org.pileka.bird_service.logging;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void preservesIncomingCorrelationId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/birds/1");
        request.addHeader(CorrelationId.HEADER_NAME, "incoming-correlation-id");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> mdcValueDuringChain = new AtomicReference<>();

        filter.doFilter(request, response, chainCapturingMdc(mdcValueDuringChain));

        assertThat(mdcValueDuringChain.get()).isEqualTo("incoming-correlation-id");
        assertThat(response.getHeader(CorrelationId.HEADER_NAME)).isEqualTo("incoming-correlation-id");
        assertThat(MDC.get(CorrelationId.MDC_KEY)).isNull();
    }

    @Test
    void generatesCorrelationIdWhenHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/birds/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> mdcValueDuringChain = new AtomicReference<>();

        filter.doFilter(request, response, chainCapturingMdc(mdcValueDuringChain));

        assertThat(mdcValueDuringChain.get()).isNotBlank();
        assertThatCode(() -> UUID.fromString(mdcValueDuringChain.get())).doesNotThrowAnyException();
        assertThat(response.getHeader(CorrelationId.HEADER_NAME)).isEqualTo(mdcValueDuringChain.get());
        assertThat(MDC.get(CorrelationId.MDC_KEY)).isNull();
    }

    @Test
    void doesNotClearUnrelatedMdcEntries() throws Exception {
        MDC.put("unrelated", "value");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/birds/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, (req, res) -> {
        });

        assertThat(MDC.get("unrelated")).isEqualTo("value");
    }

    private FilterChain chainCapturingMdc(AtomicReference<String> capturedValue) {
        return (request, response) -> capturedValue.set(MDC.get(CorrelationId.MDC_KEY));
    }
}
