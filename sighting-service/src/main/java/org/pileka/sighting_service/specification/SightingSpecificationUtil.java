package org.pileka.sighting_service.specification;

import lombok.experimental.UtilityClass;
import org.pileka.sighting_service.dto.QuerySightingDTO;
import org.pileka.sighting_service.model.Sighting;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.time.LocalDateTime;

@UtilityClass
public class SightingSpecificationUtil {

    public PredicateSpecification<Sighting> hasUserId(Long userId) {
        return (root, builder) ->
                userId == null ? builder.conjunction() :
                        builder.equal(root.get("userId"), userId);
    }

    public PredicateSpecification<Sighting> hasBirdId(Long birdId) {
        return (root, builder) ->
                birdId == null ? builder.conjunction() :
                        builder.equal(root.get("birdId"), birdId);
    }

    public PredicateSpecification<Sighting> sightedAfter(LocalDateTime dateTime) {
        return (root, builder) ->
                dateTime == null ? builder.conjunction() :
                        builder.greaterThanOrEqualTo(root.get("sightedAt"), dateTime);
    }

    public PredicateSpecification<Sighting> sightedBefore(LocalDateTime dateTime) {
        return (root, builder) ->
                dateTime == null ? builder.conjunction() :
                        builder.lessThanOrEqualTo(root.get("sightedAt"), dateTime);
    }

    public PredicateSpecification<Sighting> sightedBetween(LocalDateTime from, LocalDateTime to) {
        return sightedAfter(from).and(sightedBefore(to));
    }


    public PredicateSpecification<Sighting> buildFromQueryDTO(QuerySightingDTO queryDto) {
        if (queryDto == null) {
            return (root, builder) -> builder.conjunction();
        }

        return PredicateSpecification
                .where(hasUserId(queryDto.getUserId()))
                .and(hasBirdId(queryDto.getBirdId()))
                .and(sightedAfter(queryDto.getSightedFrom()))
                .and(sightedBefore(queryDto.getSightedTo()));
    }
}