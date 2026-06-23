package org.pileka.birdwatching_security_commons.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    private String secret;

    private long bearerTokenExpiration = 900_000L;

    private long refreshTokenExpiration = 604_800_000L;

    private String issuer = "org.pileka.birdwatching-microservices";
}
