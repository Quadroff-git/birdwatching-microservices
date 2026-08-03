package org.pileka.birdwatching_inter_service_auth_commons.initializer;

import org.pileka.birdwatching_security_commons.jwt.JWTProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInitializer;

import java.util.HashSet;
import java.util.Set;

/**
 * Intercepts all outgoing HTTP requests and adds a valid JWT token to them without directly issuing valid jwts in service code
 */
public class JWTRequestInitializer implements ClientHttpRequestInitializer {

    private final JWTProvider jwtProvider;

    @Value("${spring.application.name}")
    private static String serviceName;

    public JWTRequestInitializer(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void initialize(ClientHttpRequest request) {
        request.getHeaders().add("Authorization", "Bearer " + jwtProvider.generateBearerToken(serviceName, new HashSet<>(Set.of("ADMIN"))));
    }
}
