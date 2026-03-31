package org.pileka.bird_service.mapper;


import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.test_util.BirdTestUtil;


public class BirdMapperTest {

    BirdMapper birdMapper;

    BirdMapperTest() {
        this.birdMapper = Mappers.getMapper(BirdMapper.class);
    }

    @Test
    void toDtoMapsCorrectly() {
        // Create and populate entity
        Bird bird = BirdTestUtil.getBird();

        ResponseBirdDTO dto = birdMapper.toDto(bird);

        BirdTestUtil.assertDtoEqualsModel(dto, bird);
    }

    @Test
    void toModelMapsCorrectly() {
        CreateUpdateBirdDTO dto = BirdTestUtil.getCreateUpdateBirdDto();

        Bird bird = birdMapper.toModel(dto);

        BirdTestUtil.assertModelEqualsDto(bird, dto);
    }

}
