package org.pileka.sighting_service.audit;

import org.junit.jupiter.api.Test;
import org.pileka.birdwatching_audit_commons.audit.AuditAccessInterceptor;
import org.pileka.birdwatching_audit_commons.audit.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuditFileTest {

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Test
    void writesAuditEntriesToSeparateFileOnly() throws Exception {
        String action = "AUDIT_FILE_TEST_" + UUID.randomUUID();
        Path auditLog = Path.of("logs/audit.log");
        Path mainLog = Path.of("logs/application.log");

        auditLogger.success(action, "details=separate-file");

        assertThat(auditLog).exists();
        assertThat(Files.readString(auditLog)).contains("action=" + action);
        assertThat(Files.exists(mainLog) && Files.readString(mainLog).contains(action)).isFalse();
    }

    @Test
    void registersAccessAuditInterceptor() {
        assertThat(handlerMapping.getAdaptedInterceptors())
                .anyMatch(AuditAccessInterceptor.class::isInstance);
    }
}
