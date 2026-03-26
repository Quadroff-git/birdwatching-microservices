package org.pileka.bird_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.pileka.bird_service.model.ConservationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseBirdDTO {
    private Long id;

    private String commonName;

    private String scientificName;

    private String family;

    private ConservationStatus conservationStatus;

    private String typicalHabitat;

    private String description;

    private String imageUrl;

    private Double averageWingspan; // in cm

    private Double averageWeight; // in grams

    private String diet;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
