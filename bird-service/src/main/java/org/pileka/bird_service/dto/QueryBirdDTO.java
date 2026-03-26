package org.pileka.bird_service.dto;

import org.pileka.bird_service.model.ConservationStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class QueryBirdDTO {

    @Size(min = 2, max = 100, message = "Common name must be between 2 and 100 characters")
    private String commonName;

    @Size(min = 2, max = 150, message = "Scientific name must be between 2 and 150 characters")
    private String scientificName;

    @Size(max = 100, message = "Family name must not exceed 100 characters")
    private String family;

    private ConservationStatus conservationStatus;

    @Size(max = 200, message = "Typical habitat must not exceed 200 characters")
    private String typicalHabitat;

    @DecimalMin(value = "0.1", message = "Minimum wingspan must be at least 0.1 cm")
    @DecimalMax(value = "500", message = "Minimum wingspan must not exceed 500 cm")
    private Double minWingspan;

    @DecimalMin(value = "0.1", message = "Maximum wingspan must be at least 0.1 cm")
    @DecimalMax(value = "500", message = "Maximum wingspan must not exceed 500 cm")
    private Double maxWingspan;

    @DecimalMin(value = "0.1", message = "Minimum weight must be at least 0.1 grams")
    @DecimalMax(value = "50000", message = "Minimum weight must not exceed 50000 grams")
    private Double minWeight;

    @DecimalMin(value = "0.1", message = "Maximum weight must be at least 0.1 grams")
    @DecimalMax(value = "50000", message = "Maximum weight must not exceed 50000 grams")
    private Double maxWeight;

    @Size(max = 200, message = "Diet must not exceed 200 characters")
    private String diet;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAfter;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdBefore;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAfter;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedBefore;

    @AssertTrue(message = "Min wingspan must be less than or equal to max wingspan")
    private boolean isWingspanRangeValid() {
        if (minWingspan == null || maxWingspan == null) return true;
        return minWingspan <= maxWingspan;
    }

    @AssertTrue(message = "Min weight must be less than or equal to max weight")
    private boolean isWeightRangeValid() {
        if (minWeight == null || maxWeight == null) return true;
        return minWeight <= maxWeight;
    }

    @AssertTrue(message = "Created after date must be before created before date")
    private boolean isCreatedDateRangeValid() {
        if (createdAfter == null || createdBefore == null) return true;
        return createdAfter.isBefore(createdBefore);
    }

    @AssertTrue(message = "Updated after date must be before updated before date")
    private boolean isUpdatedDateRangeValid() {
        if (updatedAfter == null || updatedBefore == null) return true;
        return updatedAfter.isBefore(updatedBefore);
    }
}