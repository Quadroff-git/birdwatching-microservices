package org.pileka.birdwatching_security_commons.jwt.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.pileka.birdwatching_security_commons.config.JWTProperties;
import org.pileka.birdwatching_security_commons.jwt.JWTProvider;
import org.springframework.util.Assert;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class JWTProviderImpl implements JWTProvider {

    private final JWTProperties jwtProperties;

    private SecretKey getSigningKey() {
        Assert.hasText(jwtProperties.getSecret(), "jwt.secret must be configured");
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    @Override
    public Long getBearerTokenExpiration() {
        return jwtProperties.getBearerTokenExpiration();
    }

    @Override
    public Long getRefreshTokenExpiration() {
        return jwtProperties.getRefreshTokenExpiration();
    }

    @Override
    public String generateBearerToken(String sub, Set<String> roles) {
        return Jwts.builder()
                .header()
                .add(getCommonHeaders())
                .and()
                .claims()
                    .add(getCommonClaims(jwtProperties.getBearerTokenExpiration()))
                    .subject(sub)
                    .add("roles", roles)
                    .and()
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean tokenIsValid(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration().after(new Date());
        }
        catch (JwtException ex) {
            return false;
        }
    }

    @Override
    public String getSubFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public Set<String> getRolesFromToken(String token) {
        return new HashSet<>(Set.of(
                Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles")
                .toString()
                .split(", "))
        );
    }

    private Header getCommonHeaders() {
        return Jwts.header()
                .type("JWT")
                .build();
    }

    private Claims getCommonClaims(long expiration) {
        return Jwts.claims()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .build();
    }
}
