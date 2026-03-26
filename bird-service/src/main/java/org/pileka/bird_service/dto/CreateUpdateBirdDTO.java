package org.pileka.bird_service.dto;

import org.pileka.bird_service.model.ConservationStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUpdateBirdDTO {

    @NotBlank(message = "Common name is required")
    @Size(min = 2, max = 100, message = "Common name must be between 2 and 100 characters")
    private String commonName;

    @NotBlank(message = "Scientific name is required")
    @Size(min = 2, max = 150, message = "Scientific name must be between 2 and 150 characters")
    @Pattern(regexp = "^[A-Z][a-z]+ [a-z]+$",
            message = "Scientific name must be in format 'Genus species' (e.g., 'Haliaeetus leucocephalus')")
    private String scientificName;

    @Size(max = 100, message = "Family name must not exceed 100 characters")
    private String family;

    @NotNull(message = "Conservation status is required")
    private ConservationStatus conservationStatus;

    @Size(max = 200, message = "Typical habitat must not exceed 200 characters")
    private String typicalHabitat;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Pattern(regexp = "^(http|https)://.*$",
            message = "Image URL must be a valid HTTP/HTTPS URL")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @DecimalMin(value = "0.1", message = "Average wingspan must be at least 0.1 cm")
    @DecimalMax(value = "500", message = "Average wingspan must not exceed 500 cm")
    private Double averageWingspan;

    @DecimalMin(value = "0.1", message = "Average weight must be at least 0.1 grams")
    @DecimalMax(value = "50000", message = "Average weight must not exceed 50000 grams (50kg)")
    private Double averageWeight;

    @Size(max = 200, message = "Diet description must not exceed 200 characters")
    private String diet;
}