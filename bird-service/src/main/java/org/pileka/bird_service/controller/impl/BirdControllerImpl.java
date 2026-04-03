package org.pileka.bird_service.controller.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.bird_service.controller.BirdController;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.service.BirdService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BirdControllerImpl implements BirdController {

    private final BirdService birdService;

    @Override
    public ResponseBirdDTO createBird(CreateUpdateBirdDTO createDto) {
        return birdService.create(createDto);
    }

    @Override
    public ResponseBirdDTO getBirdById(long id) {
        return birdService.getById(id);
    }

    @Override
    public Page<ResponseBirdDTO> getBirds(QueryBirdDTO queryDto, Pageable pageable) {
        return birdService.get(queryDto, pageable);
    }

    @Override
    public ResponseBirdDTO updateBird(long id, CreateUpdateBirdDTO updateDto) {
        return birdService.update(id, updateDto);
    }

    @Override
    public ResponseBirdDTO deleteBird(long id) {
        return birdService.delete(id);
    }
}
