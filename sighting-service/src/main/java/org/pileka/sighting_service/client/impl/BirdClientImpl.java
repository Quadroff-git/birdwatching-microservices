package org.pileka.sighting_service.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.pileka.sighting_service.client.BirdClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
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
        log.info("Checking whether bird with id={} exists", id);
        return resourceExists(id);
    }
}
