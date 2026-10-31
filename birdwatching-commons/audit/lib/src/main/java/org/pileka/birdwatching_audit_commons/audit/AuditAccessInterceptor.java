package org.pileka.birdwatching_audit_commons.audit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Records the authenticated request boundary as an audit {@code LOGIN}/{@code LOGOUT} pair.
 * In this stateless JWT application a request is the only session boundary available to a
 * resource service; real login/logout are owned by user-service.
 */
public class AuditAccessInterceptor implements HandlerInterceptor {

    public static final String LOGIN_ACTION = "LOGIN";

    public static final String LOGOUT_ACTION = "LOGOUT";

    private static final String AUDITED_REQUEST_ATTRIBUTE =
            AuditAccessInterceptor.class.getName() + ".audited";

    private final AuditLogger auditLogger;

    public AuditAccessInterceptor(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (AuditUserResolver.resolve() == null) {
            return true;
        }

        request.setAttribute(AUDITED_REQUEST_ATTRIBUTE, Boolean.TRUE);
        auditLogger.success(LOGIN_ACTION, requestDetails(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        if (request.getAttribute(AUDITED_REQUEST_ATTRIBUTE) == null) {
            return;
        }

        boolean success = ex == null && response.getStatus() < 400;
        auditLogger.log(LOGOUT_ACTION, success,
                requestDetails(request) + " status=" + response.getStatus());
    }

    private String requestDetails(HttpServletRequest request) {
        return "request=" + request.getMethod() + " " + request.getRequestURI();
    }
}
