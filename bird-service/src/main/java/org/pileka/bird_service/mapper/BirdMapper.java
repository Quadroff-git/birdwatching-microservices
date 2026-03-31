package org.pileka.bird_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.model.Bird;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BirdMapper {

    ResponseBirdDTO toDto(Bird bird);

    Bird toModel(CreateUpdateBirdDTO dto);

    void update(@MappingTarget Bird bird, CreateUpdateBirdDTO dto);
}
