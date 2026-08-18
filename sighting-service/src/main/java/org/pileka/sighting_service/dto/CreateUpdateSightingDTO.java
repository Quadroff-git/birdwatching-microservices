package org.pileka.sighting_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pileka.sighting_service.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateUpdateSightingDTO {

    private Long birdId;

    private Long userId;

    private LocalDateTime sightedAt;

    private Location location;

}
