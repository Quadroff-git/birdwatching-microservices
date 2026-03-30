package org.pileka.bird_service.service;

import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;


public interface BirdService {

    @PreAuthorize("hasAnyRole('ADMIN', 'RESEARCHER')")
    ResponseBirdDTO create(CreateUpdateBirdDTO createDto);

    ResponseBirdDTO getById(long id);

    Page<ResponseBirdDTO> get(QueryBirdDTO queryDto, Pageable pageable);

    @PreAuthorize("hasAnyRole('ADMIN', 'RESEARCHER')")
    ResponseBirdDTO update(long id, CreateUpdateBirdDTO updateDto);

    @PreAuthorize("hasAnyRole('ADMIN', 'RESEARCHER')")
    ResponseBirdDTO delete(long id);

}
