package org.pileka.bird_service.mapper;


import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.model.ConservationStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BirdMapperTest {

    BirdMapper birdMapper;

    BirdMapperTest() {
        this.birdMapper = Mappers.getMapper(BirdMapper.class);
    }

    @Test
    void toDtoMapsCorrectly() {
        // Create and populate entity
        Bird peregrineFalcon = new Bird();
        peregrineFalcon.setId(1L);
        peregrineFalcon.setCommonName("Peregrine Falcon");
        peregrineFalcon.setScientificName("Falco peregrinus");
        peregrineFalcon.setFamily("Falconidae");
        peregrineFalcon.setConservationStatus(ConservationStatus.LEAST_CONCERN);
        peregrineFalcon.setTypicalHabitat("Wide variety of habitats from mountains to cities");
        peregrineFalcon.setDescription(
                "The peregrine falcon is known for its incredible speed, reaching over 320 km/h during hunting dives."
        );
        peregrineFalcon.setImageUrl("https://example.com/images/peregrine-falcon.jpg");
        peregrineFalcon.setAverageWingspan(100.0);
        peregrineFalcon.setAverageWeight(950.0);
        peregrineFalcon.setDiet("Medium-sized birds like pigeons and doves");

        LocalDateTime now = LocalDateTime.now();
        peregrineFalcon.setCreatedAt(now);
        peregrineFalcon.setUpdatedAt(now);

        // Map to DTO
        ResponseBirdDTO dto = birdMapper.toDto(peregrineFalcon);

        // Assert all fields match
        assertEquals(peregrineFalcon.getId(), dto.getId());
        assertEquals(peregrineFalcon.getCommonName(), dto.getCommonName());
        assertEquals(peregrineFalcon.getScientificName(), dto.getScientificName());
        assertEquals(peregrineFalcon.getFamily(), dto.getFamily());
        assertEquals(peregrineFalcon.getConservationStatus(), dto.getConservationStatus());
        assertEquals(peregrineFalcon.getTypicalHabitat(), dto.getTypicalHabitat());
        assertEquals(peregrineFalcon.getDescription(), dto.getDescription());
        assertEquals(peregrineFalcon.getImageUrl(), dto.getImageUrl());
        assertEquals(peregrineFalcon.getAverageWingspan(), dto.getAverageWingspan());
        assertEquals(peregrineFalcon.getAverageWeight(), dto.getAverageWeight());
        assertEquals(peregrineFalcon.getDiet(), dto.getDiet());
        assertEquals(peregrineFalcon.getCreatedAt(), dto.getCreatedAt());
        assertEquals(peregrineFalcon.getUpdatedAt(), dto.getUpdatedAt());
    }

}
