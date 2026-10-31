package org.pileka.birdwatching_audit_commons.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes user-action audit entries to the dedicated {@code AUDIT} logger.
 * The logger and its appenders are configured in each service's {@code logback-spring.xml}.
 */
public class AuditLogger {

    public static final String LOGGER_NAME = "AUDIT";

    private static final String MESSAGE_FORMAT = "user={} action={} success={} details={}";

    private final Logger logger;

    public AuditLogger() {
        this(LoggerFactory.getLogger(LOGGER_NAME));
    }

    AuditLogger(Logger logger) {
        this.logger = logger;
    }

    public void success(String action, String details) {
        log(action, true, details);
    }

    public void failure(String action, String details, Throwable cause) {
        String reason = cause == null ? "unknown" : cause.getClass().getSimpleName();
        String failureDetails = details == null || details.isBlank()
                ? "reason=" + reason
                : details + " reason=" + reason;
        log(action, false, failureDetails);
    }

    public void log(String action, boolean success, String details) {
        String safeDetails = details == null || details.isBlank() ? "-" : details;
        String user = resolveUser();

        if (success) {
            logger.info(MESSAGE_FORMAT, user, action, true, safeDetails);
        } else {
            logger.warn(MESSAGE_FORMAT, user, action, false, safeDetails);
        }
    }

    private String resolveUser() {
        String user = AuditUserResolver.resolve();
        return user == null ? "anonymous" : user;
    }
}
