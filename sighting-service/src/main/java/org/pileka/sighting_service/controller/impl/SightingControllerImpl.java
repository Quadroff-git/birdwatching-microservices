package org.pileka.sighting_service.controller.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.sighting_service.controller.SightingController;
import org.pileka.sighting_service.dto.CreateUpdateSightingDTO;
import org.pileka.sighting_service.dto.QuerySightingDTO;
import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.pileka.sighting_service.service.SightingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SightingControllerImpl implements SightingController {

    private final SightingService sightingService;

    @Override
    public ResponseSightingDTO createSighting(CreateUpdateSightingDTO createDto) {
        return sightingService.create(createDto);
    }

    @Override
    public ResponseSightingDTO getSightingById(long id) {
        return sightingService.getById(id);
    }

    @Override
    public Page<ResponseSightingDTO> getSightings(QuerySightingDTO queryDto, Pageable pageable) {
        return sightingService.get(queryDto, pageable);
    }

    @Override
    public ResponseSightingDTO updateSighting(long id, CreateUpdateSightingDTO updateDto) {
        return sightingService.update(id, updateDto);
    }

    @Override
    public ResponseSightingDTO deleteSighting(long id) {
        return sightingService.delete(id);
    }
}
