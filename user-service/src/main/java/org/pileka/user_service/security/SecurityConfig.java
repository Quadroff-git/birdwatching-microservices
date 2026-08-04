package org.pileka.user_service.security;

import org.pileka.birdwatching_security_commons.config.JWTProperties;
import org.pileka.birdwatching_security_commons.jwt.JWTProvider;
import org.pileka.birdwatching_security_commons.jwt.impl.JWTProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.function.SingletonSupplier;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean(name = "AuthSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .securityMatcher("/api/auth/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return SingletonSupplier.of(PasswordEncoderFactories::createDelegatingPasswordEncoder).get();
    }

    @Bean
    public JWTProvider jwtProvider(JWTProperties jwtProperties) {
        return new JWTProviderImpl(jwtProperties);
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager(JWTAuthenticationProviderAdapter jwtAuthenticationProvider,
                                                       UserDetailsService userDetailsService) {
        return new ProviderManager(
                List.of(
                        jwtAuthenticationProvider,
                        new DaoAuthenticationProvider(userDetailsService)
                )
        );
    }
}
