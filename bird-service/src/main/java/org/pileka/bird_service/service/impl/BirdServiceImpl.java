package org.pileka.bird_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.exception.EntityDoesntExistException;
import org.pileka.bird_service.mapper.BirdMapper;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.repository.BirdRepository;
import org.pileka.bird_service.service.BirdService;
import org.pileka.bird_service.specification.BirdSpecificationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class BirdServiceImpl implements BirdService {

    private final BirdRepository birdRepository;
    private final BirdMapper birdMapper;

    @Override
    public ResponseBirdDTO create(CreateUpdateBirdDTO createDto) {
        return birdMapper.toDto(
                birdRepository.save(
                        birdMapper.toModel(createDto)
                )
        );
    }

    @Override
    public ResponseBirdDTO getById(long id) {
        return doWithFoundById(id,
                birdMapper::toDto,
                "Bird with id %d doesn't exist and can't be fetched.".formatted(id)
        );
    }

    @Override
    public Page<ResponseBirdDTO> get(QueryBirdDTO queryDto, Pageable pageable) {
        return birdRepository.findBy(
                BirdSpecificationUtil.buildFromQueryDTO(queryDto),
                p -> p.page(pageable)
        ).map(birdMapper::toDto);
    }

    @Override
    public ResponseBirdDTO update(long id, CreateUpdateBirdDTO updateDto) {
        return  doWithFoundById(id,
                bird -> {
                    birdMapper.update(bird, updateDto);
                    birdRepository.save(bird);

                    return birdMapper.toDto(bird);
                },
                "Bird with id %d doesn't exist and can't be updated.".formatted(id)
        );
    }

    @Override
    public ResponseBirdDTO delete(long id) {
        return doWithFoundById(id,
                bird -> {
                    birdRepository.delete(bird);
                    return birdMapper.toDto(bird);
                },
                "Bird with id %d doesn't exist and can't be deleted.".formatted(id)
        );
    }

    /**
     * Convenience method to avoid the ugly if-block optional handling
     * @param id id of the entity to find
     * @param function function that will receive the found entity as argument
     * @param exceptionMsg message to use when throwing {@link EntityDoesntExistException} in case the entity isn't found
     * @return whatever {@code function} returns
     */
    private ResponseBirdDTO doWithFoundById(long id, Function<Bird, ResponseBirdDTO> function, String exceptionMsg) {
        Optional<Bird> optionalBird = birdRepository.findById(id);
        if (optionalBird.isPresent()) {
            return function.apply(optionalBird.get());
        }
        else {
            throw new EntityDoesntExistException(exceptionMsg);
        }
    }
}
