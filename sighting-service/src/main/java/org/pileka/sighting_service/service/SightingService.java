package org.pileka.sighting_service.service;

import org.pileka.sighting_service.dto.CreateUpdateSightingDTO;
import org.pileka.sighting_service.dto.QuerySightingDTO;
import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;


public interface SightingService {

    @PreAuthorize("authentication.name == #createDto.getUserId().toString() || hasRole('ADMIN')")
    ResponseSightingDTO create(CreateUpdateSightingDTO createDto);

    ResponseSightingDTO getById(long id);

    Page<ResponseSightingDTO> get(QuerySightingDTO queryDto, Pageable pageable);

    @PreAuthorize("authentication.name == #updateDto.getUserId().toString() || hasRole('ADMIN')")
    ResponseSightingDTO update(long id, CreateUpdateSightingDTO updateDto);

    @PreAuthorize("authentication.name == #id.toString() || hasRole('ADMIN')")
    ResponseSightingDTO delete(long id);
    
}
