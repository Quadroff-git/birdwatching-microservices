package org.pileka.birdwatching_inter_service_auth_commons.config;

import org.pileka.birdwatching_inter_service_auth_commons.initializer.JWTRequestInitializer;
import org.pileka.birdwatching_security_commons.config.JWTProperties;
import org.pileka.birdwatching_security_commons.jwt.impl.JWTProviderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication
public class InterServiceAuthConfig {
    @Bean
    RestClientCustomizer restClientCustomizer(JWTProperties jwtProperties, @Value("${spring.application.name}") String serviceName) {
        return (restClientBuilder) ->
                restClientBuilder.requestInitializer(
                    new JWTRequestInitializer(new JWTProviderImpl(jwtProperties), serviceName)
                );
    }
}
