package org.pileka.bird_service.specification;

import lombok.experimental.UtilityClass;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.model.Bird_;
import org.pileka.bird_service.model.ConservationStatus;
import org.springframework.data.jpa.domain.PredicateSpecification;
import java.time.LocalDateTime;

@UtilityClass
public class BirdSpecificationUtil {

    // Individual specification methods

    public PredicateSpecification<Bird> commonNameContains(String commonName) {
        return (from, builder) ->
                commonName == null ? builder.conjunction() :
                        builder.like(builder.lower(from.get(Bird_.commonName)),
                                "%" + commonName.toLowerCase() + "%");
    }

    public PredicateSpecification<Bird> scientificNameContains(String scientificName) {
        return (from, builder) ->
                scientificName == null ? builder.conjunction() :
                        builder.like(builder.lower(from.get(Bird_.scientificName)),
                                "%" + scientificName.toLowerCase() + "%");
    }

    public PredicateSpecification<Bird> familyContains(String family) {
        return (from, builder) ->
                family == null ? builder.conjunction() :
                        builder.like(builder.lower(from.get(Bird_.family)),
                                "%" + family.toLowerCase() + "%");
    }

    public PredicateSpecification<Bird> conservationStatusEquals(ConservationStatus status) {
        return (from, builder) ->
                status == null ? builder.conjunction() :
                        builder.equal(from.get(Bird_.conservationStatus), status);
    }

    public PredicateSpecification<Bird> typicalHabitatContains(String habitat) {
        return (from, builder) ->
                habitat == null ? builder.conjunction() :
                        builder.like(builder.lower(from.get(Bird_.typicalHabitat)),
                                "%" + habitat.toLowerCase() + "%");
    }

    // Split wingspan methods
    public PredicateSpecification<Bird> wingspanAbove(Double min) {
        return (from, builder) ->
                min == null ? builder.conjunction() :
                        builder.greaterThanOrEqualTo(from.get(Bird_.averageWingspan), min);
    }

    public PredicateSpecification<Bird> wingspanBelow(Double max) {
        return (from, builder) ->
                max == null ? builder.conjunction() :
                        builder.lessThanOrEqualTo(from.get(Bird_.averageWingspan), max);
    }

    // Split weight methods
    public PredicateSpecification<Bird> weightAbove(Double min) {
        return (from, builder) ->
                min == null ? builder.conjunction() :
                        builder.greaterThanOrEqualTo(from.get(Bird_.averageWeight), min);
    }

    public PredicateSpecification<Bird> weightBelow(Double max) {
        return (from, builder) ->
                max == null ? builder.conjunction() :
                        builder.lessThanOrEqualTo(from.get(Bird_.averageWeight), max);
    }

    public PredicateSpecification<Bird> dietContains(String diet) {
        return (from, builder) ->
                diet == null ? builder.conjunction() :
                        builder.like(builder.lower(from.get(Bird_.diet)),
                                "%" + diet.toLowerCase() + "%");
    }

    public PredicateSpecification<Bird> createdAfter(LocalDateTime date) {
        return (from, builder) ->
                date == null ? builder.conjunction() :
                        builder.greaterThanOrEqualTo(from.get(Bird_.createdAt), date);
    }

    public PredicateSpecification<Bird> createdBefore(LocalDateTime date) {
        return (from, builder) ->
                date == null ? builder.conjunction() :
                        builder.lessThanOrEqualTo(from.get(Bird_.createdAt), date);
    }

    public PredicateSpecification<Bird> updatedAfter(LocalDateTime date) {
        return (from, builder) ->
                date == null ? builder.conjunction() :
                        builder.greaterThanOrEqualTo(from.get(Bird_.updatedAt), date);
    }

    public PredicateSpecification<Bird> updatedBefore(LocalDateTime date) {
        return (from, builder) ->
                date == null ? builder.conjunction() :
                        builder.lessThanOrEqualTo(from.get(Bird_.updatedAt), date);
    }

    // Complete specification builder from QueryBirdDTO
    public PredicateSpecification<Bird> buildFromQueryDTO(QueryBirdDTO query) {
        if (query == null) {
            return (from, builder) -> builder.conjunction();
        }

        return PredicateSpecification
                .where(commonNameContains(query.getCommonName()))
                .and(scientificNameContains(query.getScientificName()))
                .and(familyContains(query.getFamily()))
                .and(conservationStatusEquals(query.getConservationStatus()))
                .and(typicalHabitatContains(query.getTypicalHabitat()))
                .and(wingspanAbove(query.getMinWingspan()))
                .and(wingspanBelow(query.getMaxWingspan()))
                .and(weightAbove(query.getMinWeight()))
                .and(weightBelow(query.getMaxWeight()))
                .and(dietContains(query.getDiet()))
                .and(createdAfter(query.getCreatedAfter()))
                .and(createdBefore(query.getCreatedBefore()))
                .and(updatedAfter(query.getUpdatedAfter()))
                .and(updatedBefore(query.getUpdatedBefore()));
    }
}