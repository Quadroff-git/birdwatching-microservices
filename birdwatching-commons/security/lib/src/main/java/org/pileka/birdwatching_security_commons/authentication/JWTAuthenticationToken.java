package org.pileka.birdwatching_security_commons.authentication;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * An {@link Authentication} implementation for use with this project's custom JWT auth system.
 */
@Getter
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String sub;
    private final String jwt;

    /**
     * Constructor intended for any code (mostly the related filter)
     * @param jwt token to validate
     */
    public JWTAuthenticationToken(String jwt) {
        super((Collection<? extends GrantedAuthority>) null);
        this.jwt = jwt;

        this.sub = "";
    }

    /**
     * Constructor intended for use by {@link org.springframework.security.authentication.AuthenticationManager} and
     * {@link org.springframework.security.authentication.AuthenticationProvider} implementations to use when creating a
     * fully authenticated object. Implementation sets <code>authenticated</code> to true.
     * @param sub value of <code>sub</code> claim in the JWT
     * @param jwt token
     * @param authorities authorities as specified by <code>roles</code> claim in the JWT
     */
    public JWTAuthenticationToken(String sub, String jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.sub = sub;
        this.jwt = jwt;
        super.setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return jwt;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return sub;
    }
}
