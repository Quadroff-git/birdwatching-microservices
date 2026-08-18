package org.pileka.sighting_service.controller;

import jakarta.validation.Valid;
import org.pileka.sighting_service.dto.CreateUpdateSightingDTO;
import org.pileka.sighting_service.dto.QuerySightingDTO;
import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${api.paths.sightings}")
public interface SightingController {
    
    @PostMapping(produces = "application/json")
    ResponseSightingDTO createSighting(@Valid @RequestBody CreateUpdateSightingDTO createDto);

    @GetMapping(path = "/{id}", produces = "application/json")
    ResponseSightingDTO getSightingById(@PathVariable long id);

    @GetMapping(produces = "application/json")
    Page<ResponseSightingDTO> getSightings(@Valid QuerySightingDTO queryDto,
                                   @PageableDefault(size = 20, sort = "id") Pageable pageable);

    @PutMapping(path = "/{id}", produces = "application/json")
    ResponseSightingDTO updateSighting(@PathVariable long id, @Valid @RequestBody CreateUpdateSightingDTO updateDto);

    @DeleteMapping(path = "/{id}")
    ResponseSightingDTO deleteSighting(@PathVariable long id);
}
