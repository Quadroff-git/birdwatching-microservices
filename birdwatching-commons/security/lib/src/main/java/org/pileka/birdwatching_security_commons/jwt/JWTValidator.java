package org.pileka.birdwatching_security_commons.jwt;

import java.util.Set;

/**
 * Minimal interface for JWT validation
 */
public interface JWTValidator {
    boolean tokenIsValid(String token);

    String getUsernameFromToken(String token);

    Set<String> getRolesFromToken(String token);
}
