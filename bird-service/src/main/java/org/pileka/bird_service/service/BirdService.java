package org.pileka.bird_service.service;

import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BirdService {

    ResponseBirdDTO create(CreateUpdateBirdDTO createDto);

    ResponseBirdDTO getById(long id);

    Page<ResponseBirdDTO> get(QueryBirdDTO queryDto, Pageable pageable);

    ResponseBirdDTO update(CreateUpdateBirdDTO updateDto);

    ResponseBirdDTO delete(long id);

}
