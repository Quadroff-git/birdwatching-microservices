package org.pileka.user_service.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {

    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("token_type")
    private final String tokenType = "Bearer";

    @JsonProperty("expires_in")
    private final Long expiresIn;

}
