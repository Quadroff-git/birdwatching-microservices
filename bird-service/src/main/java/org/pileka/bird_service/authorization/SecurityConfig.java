package org.pileka.bird_service.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Require authentication for all endpoints
                )
                .addFilterBefore(new HeaderFilter(), AnonymousAuthenticationFilter.class);

        return http.build();
    }

    /**
     * "Domain" services aren't intended to do any authentication of their own. They rely on user service to authenticate
     * the request at the API gateway level and add the necessary headers to it which they can use to authorize those requests.
     * Spring Security however autoconfigures its own AuthenticationManager and an in-memory UserDetailsManager
     * implementation for it, which we don't need. To prevent this we expose a stub {@link AuthenticationManager} instance.
     * @return a stub {@link AuthenticationManager} instance which does nothing to the {@link org.springframework.security.core.Authentication}
     * instance
     */
    @Bean
    public AuthenticationManager stubAuthenticationManager() {
        return authentication -> authentication;
    }
}
