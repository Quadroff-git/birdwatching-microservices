package org.pileka.sighting_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class QuerySightingDTO {

    @Min(value = 1, message = "User ID must be a positive number")
    private Long userId;

    @Min(value = 1, message = "Bird ID must be a positive number")
    private Long birdId;

    @PastOrPresent(message = "Sighted from date cannot be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sightedFrom;

    @PastOrPresent(message = "Sighted to date cannot be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sightedTo;

    // Custom validation for date range
    @jakarta.validation.constraints.AssertTrue(message = "Sighted from date must be before sighted to date")
    public boolean isDateRangeValid() {
        if (sightedFrom == null || sightedTo == null) {
            return true;
        }
        return sightedFrom.isBefore(sightedTo) || sightedFrom.isEqual(sightedTo);
    }
}