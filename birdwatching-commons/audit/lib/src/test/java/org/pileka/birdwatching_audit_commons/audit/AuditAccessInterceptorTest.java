package org.pileka.birdwatching_audit_commons.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class AuditAccessInterceptorTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void logsLoginAndLogoutForAuthenticatedRequest() {
        authenticate("42");
        AuditLogger auditLogger = mock(AuditLogger.class);
        AuditAccessInterceptor interceptor = new AuditAccessInterceptor(auditLogger);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/birds/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
        interceptor.afterCompletion(request, response, new Object(), null);

        verify(auditLogger).success("LOGIN", "request=GET /api/birds/1");
        verify(auditLogger).log("LOGOUT", true, "request=GET /api/birds/1 status=200");
    }

    @Test
    void skipsUnauthenticatedRequest() {
        AuditLogger auditLogger = mock(AuditLogger.class);
        AuditAccessInterceptor interceptor = new AuditAccessInterceptor(auditLogger);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/birds/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThat(interceptor.preHandle(request, response, new Object())).isTrue();
        interceptor.afterCompletion(request, response, new Object(), null);

        verifyNoInteractions(auditLogger);
    }

    @Test
    void marksUnsuccessfulResponseAsFailedLogout() {
        authenticate("42");
        AuditLogger auditLogger = mock(AuditLogger.class);
        AuditAccessInterceptor interceptor = new AuditAccessInterceptor(auditLogger);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/sightings");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(422);

        interceptor.preHandle(request, response, new Object());
        interceptor.afterCompletion(request, response, new Object(), null);

        verify(auditLogger).log("LOGOUT", false, "request=POST /api/sightings status=422");
    }

    private void authenticate(String user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );
    }
}
