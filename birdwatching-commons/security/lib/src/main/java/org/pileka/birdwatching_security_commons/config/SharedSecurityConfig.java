package org.pileka.birdwatching_security_commons.config;

import org.pileka.birdwatching_security_commons.authentication.JWTAuthenticationFilter;
import org.pileka.birdwatching_security_commons.authentication.JWTAuthenticationManager;
import org.pileka.birdwatching_security_commons.jwt.JWTProvider;
import org.pileka.birdwatching_security_commons.jwt.JWTValidator;
import org.pileka.birdwatching_security_commons.jwt.impl.JWTProviderImpl;
import org.pileka.birdwatching_security_commons.web.SecurityExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(EnableWebSecurity.class)
@EnableConfigurationProperties(JWTProperties.class)
@EnableMethodSecurity
public class SharedSecurityConfig {

    @Bean
    @ConditionalOnClass(RestControllerAdvice.class)
    @ConditionalOnMissingBean(SecurityExceptionHandler.class)
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(JWTValidator.class)
    public JWTValidator jwtValidator(JWTProperties jwtProperties) {
        return new JWTProviderImpl(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean(JWTAuthenticationManager.class)
    public JWTAuthenticationManager jwtAuthenticationManager(JWTValidator jwtValidator) {
        return new JWTAuthenticationManager(jwtValidator);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authenticationManager(JWTAuthenticationManager jwtAuthenticationManager) {
        return jwtAuthenticationManager;
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    @ConditionalOnMissingBean(name = "defaultJwtSecurityFilterChain")
    public SecurityFilterChain defaultJwtSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterAfter(new JWTAuthenticationFilter(authenticationManager), LogoutFilter.class);

        return http.build();
    }
}
