package org.pileka.bird_service.controller;

import jakarta.validation.Valid;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/birds")
public interface BirdController {

    @PostMapping(produces = "application/json")
    ResponseBirdDTO createBird(@Valid @RequestBody CreateUpdateBirdDTO createDto);

    @GetMapping(path = "/{id}", produces = "application/json")
    ResponseBirdDTO getBirdById(@PathVariable long id);

    @GetMapping(produces = "application/json")
    Page<ResponseBirdDTO> getBirds(@Valid QueryBirdDTO queryDto,
                                   @PageableDefault(size = 20, sort = "commonName") Pageable pageable);

    @PutMapping(path = "/{id}", produces = "application/json")
    ResponseBirdDTO updateBird(@PathVariable long id, @Valid @RequestBody CreateUpdateBirdDTO updateDto);

    @DeleteMapping(path = "/{id}")
    ResponseBirdDTO deleteBird(@PathVariable long id);

}
