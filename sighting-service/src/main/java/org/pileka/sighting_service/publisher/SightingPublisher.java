package org.pileka.sighting_service.publisher;

import org.pileka.sighting_service.dto.ResponseSightingDTO;

public interface SightingPublisher {

    void publish(ResponseSightingDTO dto);

}
