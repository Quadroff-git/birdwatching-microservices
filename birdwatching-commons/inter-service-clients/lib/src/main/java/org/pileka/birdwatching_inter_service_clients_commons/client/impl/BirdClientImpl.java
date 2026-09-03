package org.pileka.birdwatching_inter_service_clients_commons.client.impl;

import org.pileka.birdwatching_inter_service_clients_commons.client.BirdClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BirdClientImpl extends AbstractClientImpl implements BirdClient {

    public BirdClientImpl(RestClient.Builder builder,
                          @Value("${api.gateway.host}") String gatewayHost,
                          @Value("${api.gateway.port}") String gatewayPort,
                          @Value("${server.servlet.context-path}") String contextPath,
                          @Value("${api.paths.birds}") String birdsPath) {
        super(builder, "http://%s:%s%s%s".formatted(gatewayHost, gatewayPort, contextPath, birdsPath));
    }

    @Override
    public boolean birdExists(long id) {
        return resourceExists(id);
    }
}
