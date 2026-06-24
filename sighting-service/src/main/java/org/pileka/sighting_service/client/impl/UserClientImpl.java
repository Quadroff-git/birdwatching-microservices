package org.pileka.sighting_service.client.impl;

import org.pileka.sighting_service.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Component
public class UserClientImpl extends AbstractClientImpl implements UserClient {
    public UserClientImpl(RestClient.Builder builder,
                          @Value("${api.gateway.host}") String gatewayHost,
                          @Value("${api.gateway.port}") String gatewayPort,
                          @Value("${server.servlet.context-path}") String contextPath,
                          @Value("${api.paths.users}") String usersPath) {
        super(builder, "http://%s:%s%s%s".formatted(gatewayHost, gatewayPort, contextPath, usersPath));
    }

    @Override
    public boolean userExists(long id) {
        return super.resourceExists(id);
    }

    @Override
    public String getUsernameById(long id) {
        return client
                .get()
                .uri("/{id}", id)
                .retrieve()
                .body(JsonNode.class)
                .get("username")
                .asString();
    }
}
