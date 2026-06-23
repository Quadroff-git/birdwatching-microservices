package org.pileka.birdwatching_security_commons.authentication;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.Nullable;
import org.pileka.birdwatching_security_commons.jwt.JWTValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class JWTAuthenticationManager implements AuthenticationManager {

    private static final Log log = LogFactory.getLog(JWTAuthenticationManager.class);
    private final JWTValidator jwtValidator;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof JWTAuthenticationToken jwtAuth)) {
            return null;
        }

        if (jwtValidator.tokenIsValid(jwtAuth.getJwt())) {
            log.debug("Authentication success: authenticated user with username " + jwtValidator.getUsernameFromToken(jwtAuth.getJwt()) + " via JWT");
            return new JWTAuthenticationToken(
                    jwtValidator.getUsernameFromToken(jwtAuth.getJwt()),
                    jwtAuth.getJwt(),
                    jwtValidator.getRolesFromToken(jwtAuth.getJwt()).stream()
                            .map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                            .toList()
            );
        }
        else {
            log.debug("Authentication failed: invalid JWT supplied");
            throw new BadCredentialsException("Invalid JWT supplied");
        }
    }
}
