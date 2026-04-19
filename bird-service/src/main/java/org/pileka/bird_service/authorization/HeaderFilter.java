package org.pileka.bird_service.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        String userRoles = request.getHeader("X-User-Roles");

        if (userId != null && userRoles != null) {
            // TODO: log successful authorization
            List<SimpleGrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                    .map(String::trim)
                    .map(role -> {
                        // Ensure roles have ROLE_ prefix for Spring Security
                        String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                        return new SimpleGrantedAuthority(formattedRole);
                    })
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            authentication.setDetails(request);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.trace("Successfully authenticated User with ID = " + userId + " and roles = " + userRoles);
        } else {
            logger.trace("Received an anonymous request (no X-User-Id and X-User-Roles headers supplied");
        }

        filterChain.doFilter(request, response);
    }
}
