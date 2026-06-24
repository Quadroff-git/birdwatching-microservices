package org.pileka.sighting_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.pileka.sighting_service.dto.CreateUpdateSightingDTO;
import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.pileka.sighting_service.model.Sighting;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SightingMapper {

    ResponseSightingDTO toDto(Sighting sighting);

    Sighting toModel(CreateUpdateSightingDTO dto);

    void update(@MappingTarget Sighting sighting, CreateUpdateSightingDTO dto);

}
