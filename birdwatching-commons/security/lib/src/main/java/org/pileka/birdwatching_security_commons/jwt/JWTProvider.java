package org.pileka.birdwatching_security_commons.jwt;

import java.util.Set;

/**
 * Full-featured JWT provider extending the minimal validator
 */
public interface JWTProvider extends JWTValidator{
    Long getBearerTokenExpiration();

    Long getRefreshTokenExpiration();

    String generateBearerToken(String username, Set<String> roles);

    String generateRefreshToken(String username);

}
