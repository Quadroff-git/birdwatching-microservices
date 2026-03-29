package org.pileka.bird_service.test_util;

import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.model.ConservationStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class BirdTestUtil {

    public static Bird getBird() {
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

        return peregrineFalcon;
    }

    public static CreateUpdateBirdDTO getCreateUpdateBirdDto() {
        CreateUpdateBirdDTO peregrineFalcon = new CreateUpdateBirdDTO();
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

        return peregrineFalcon;
    }

    public static void assertDtoEqualsModel(ResponseBirdDTO dto, Bird bird) {
        assertEquals(bird.getId(), dto.getId());
        assertEquals(bird.getCommonName(), dto.getCommonName());
        assertEquals(bird.getScientificName(), dto.getScientificName());
        assertEquals(bird.getFamily(), dto.getFamily());
        assertEquals(bird.getConservationStatus(), dto.getConservationStatus());
        assertEquals(bird.getTypicalHabitat(), dto.getTypicalHabitat());
        assertEquals(bird.getDescription(), dto.getDescription());
        assertEquals(bird.getImageUrl(), dto.getImageUrl());
        assertEquals(bird.getAverageWingspan(), dto.getAverageWingspan());
        assertEquals(bird.getAverageWeight(), dto.getAverageWeight());
        assertEquals(bird.getDiet(), dto.getDiet());
        assertEquals(bird.getCreatedAt(), dto.getCreatedAt());
        assertEquals(bird.getUpdatedAt(), dto.getUpdatedAt());
    }

    public static void assertModelEqualsDto(Bird bird, CreateUpdateBirdDTO dto) {
        assertEquals(dto.getCommonName(), bird.getCommonName());
        assertEquals(dto.getScientificName(), bird.getScientificName());
        assertEquals(dto.getFamily(), bird.getFamily());
        assertEquals(dto.getConservationStatus(), bird.getConservationStatus());
        assertEquals(dto.getTypicalHabitat(), bird.getTypicalHabitat());
        assertEquals(dto.getDescription(), bird.getDescription());
        assertEquals(dto.getImageUrl(), bird.getImageUrl());
        assertEquals(dto.getAverageWingspan(), bird.getAverageWingspan());
        assertEquals(dto.getAverageWeight(), bird.getAverageWeight());
        assertEquals(dto.getDiet(), bird.getDiet());
    }

}
