package org.pileka.birdwatching_audit_commons.audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Resolves the authenticated principal of the current request.
 */
final class AuditUserResolver {

    private AuditUserResolver() {
    }

    static String resolve() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null) {
            return null;
        }

        String user = authentication.getPrincipal().toString();
        return "anonymousUser".equals(user) ? null : user;
    }
}
