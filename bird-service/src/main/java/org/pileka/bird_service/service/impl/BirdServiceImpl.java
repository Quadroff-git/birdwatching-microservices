package org.pileka.bird_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.mapper.BirdMapper;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.repository.BirdRepository;
import org.pileka.bird_service.service.BirdService;
import org.pileka.bird_service.specification.BirdSpecificationUtil;
import org.pileka.bird_service.logging.PerformanceMonitor;
import org.pileka.birdwatching_audit_commons.audit.AuditLogger;
import org.pileka.birdwatching_exception_commons.exception.EntityDoesntExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class BirdServiceImpl implements BirdService {

    private final BirdRepository birdRepository;
    private final BirdMapper birdMapper;
    private final AuditLogger auditLogger;
    private final PerformanceMonitor performanceMonitor;

    @Override
    public ResponseBirdDTO create(CreateUpdateBirdDTO createDto) {
        try {
            Bird bird = birdMapper.toModel(createDto);
            ResponseBirdDTO createdBird = birdMapper.toDto(
                    performanceMonitor.measure("db.bird.save", () -> {
                        return birdRepository.save(bird);
                    })
            );
            auditLogger.success("BIRD_CREATE", "birdId=" + createdBird.getId());
            return createdBird;
        } catch (RuntimeException ex) {
            auditLogger.failure("BIRD_CREATE", createDetails(createDto), ex);
            throw ex;
        }
    }

    @Override
    public ResponseBirdDTO getById(long id) {
        log.info("Fetching bird with id={}", id);
        return doWithFoundById(id,
                birdMapper::toDto,
                "Bird with id %d doesn't exist and can't be fetched.".formatted(id)
        );
    }

    @Override
    public Page<ResponseBirdDTO> get(QueryBirdDTO queryDto, Pageable pageable) {
        return performanceMonitor.measure("db.bird.findBy", () -> {
            return birdRepository.findBy(
                    BirdSpecificationUtil.buildFromQueryDTO(queryDto),
                    p -> p.page(pageable)
            );
        }).map(birdMapper::toDto);
    }

    @Override
    public ResponseBirdDTO update(long id, CreateUpdateBirdDTO updateDto) {
        try {
            ResponseBirdDTO updatedBird = doWithFoundById(id,
                    bird -> {
                        birdMapper.update(bird, updateDto);
                        performanceMonitor.measure("db.bird.save", () -> {
                            birdRepository.save(bird);
                        });

                        return birdMapper.toDto(bird);
                    },
                    "Bird with id %d doesn't exist and can't be updated.".formatted(id)
            );
            auditLogger.success("BIRD_UPDATE", "birdId=" + id);
            return updatedBird;
        } catch (RuntimeException ex) {
            auditLogger.failure("BIRD_UPDATE", "birdId=" + id, ex);
            throw ex;
        }
    }

    @Override
    public ResponseBirdDTO delete(long id) {
        try {
            ResponseBirdDTO deletedBird = doWithFoundById(id,
                    bird -> {
                        performanceMonitor.measure("db.bird.delete", () -> {
                            birdRepository.delete(bird);
                        });
                        return birdMapper.toDto(bird);
                    },
                    "Bird with id %d doesn't exist and can't be deleted.".formatted(id)
            );
            auditLogger.success("BIRD_DELETE", "birdId=" + id);
            return deletedBird;
        } catch (RuntimeException ex) {
            auditLogger.failure("BIRD_DELETE", "birdId=" + id, ex);
            throw ex;
        }
    }

    /**
     * Convenience method to avoid the ugly if-block optional handling
     * @param id id of the entity to find
     * @param function function that will receive the found entity as argument
     * @param exceptionMsg message to use when throwing {@link EntityDoesntExistException} in case the entity isn't found
     * @return whatever {@code function} returns
     */
    private ResponseBirdDTO doWithFoundById(long id, Function<Bird, ResponseBirdDTO> function, String exceptionMsg) {
        Optional<Bird> optionalBird = performanceMonitor.measure("db.bird.findById", () -> {
            return birdRepository.findById(id);
        });
        if (optionalBird.isPresent()) {
            return function.apply(optionalBird.get());
        }
        else {
            throw new EntityDoesntExistException(exceptionMsg);
        }
    }

    private String createDetails(CreateUpdateBirdDTO createDto) {
        return createDto == null ? "commonName=-" : "commonName=" + createDto.getCommonName();
    }
}
