package org.pileka.sighting_service.client.impl;

import org.pileka.birdwatching_exception_commons.exception.EntityDoesntExistException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

public class AbstractClientImpl {

    protected RestClient client;

    public AbstractClientImpl(RestClient.Builder builder, String baseUrl) {
        this.client = builder
                .baseUrl(baseUrl)
                .build();
    }

    public boolean resourceExists(long id) {
        try {
            if (client
                    .get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus((code) -> code.isSameCodeAs(HttpStatusCode.valueOf(404)),
                            (request, response) -> {throw new EntityDoesntExistException("skebob");} )
                    .toEntity(Object.class)
                    .getStatusCode() == HttpStatusCode.valueOf(200)) return true;
        } catch (EntityDoesntExistException ex) {
            return false;
        }

        return false;
    }

}
