package org.pileka.user_service.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.pileka.birdwatching_security_commons.authentication.JWTAuthenticationManager;
import org.pileka.birdwatching_security_commons.authentication.JWTAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationProviderAdapter implements AuthenticationProvider {

    private final JWTAuthenticationManager manager;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return manager.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JWTAuthenticationToken.class);
    }
}
