package org.pileka.birdwatching_audit_commons.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditLoggerTest {

    private static final String MESSAGE_FORMAT = "user={} action={} success={} details={}";

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void logsSuccessfulActionWithAuthenticatedUser() {
        authenticate("42");
        Logger logger = mock(Logger.class);

        new AuditLogger(logger).success("BIRD_CREATE", "birdId=1");

        verify(logger).info(MESSAGE_FORMAT, "42", "BIRD_CREATE", true, "birdId=1");
    }

    @Test
    void logsFailedActionWithReason() {
        authenticate("42");
        Logger logger = mock(Logger.class);

        new AuditLogger(logger).failure("BIRD_UPDATE", "birdId=1", new IllegalStateException("boom"));

        verify(logger).warn(MESSAGE_FORMAT, "42", "BIRD_UPDATE", false,
                "birdId=1 reason=IllegalStateException");
    }

    @Test
    void fallsBackToAnonymousWhenThereIsNoAuthentication() {
        Logger logger = mock(Logger.class);

        new AuditLogger(logger).success("SIGHTING_DELETE", "sightingId=5");

        verify(logger).info(MESSAGE_FORMAT, "anonymous", "SIGHTING_DELETE", true, "sightingId=5");
    }

    private void authenticate(String user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );
    }
}
