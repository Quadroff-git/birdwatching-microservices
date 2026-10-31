package org.pileka.sighting_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pileka.birdwatching_audit_commons.audit.AuditLogger;
import org.pileka.birdwatching_exception_commons.exception.EntityDoesntExistException;
import org.pileka.birdwatching_exception_commons.exception.ReferredEntityDoesntExistException;
import org.pileka.sighting_service.client.BirdClient;
import org.pileka.sighting_service.client.UserClient;
import org.pileka.sighting_service.dto.CreateUpdateSightingDTO;
import org.pileka.sighting_service.dto.QuerySightingDTO;
import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.pileka.sighting_service.mapper.SightingMapper;
import org.pileka.sighting_service.model.Sighting;
import org.pileka.sighting_service.publisher.SightingPublisher;
import org.pileka.sighting_service.repository.SightingRepository;
import org.pileka.sighting_service.service.SightingService;
import org.pileka.sighting_service.specification.SightingSpecificationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SightingServiceImpl implements SightingService {

    private final SightingRepository sightingRepository;
    private final SightingMapper sightingMapper;
    private final BirdClient birdClient;
    private final UserClient userClient;
    private final SightingPublisher sightingPublisher;
    private final AuditLogger auditLogger;

    @Override
    public ResponseSightingDTO create(CreateUpdateSightingDTO createDto) {
        try {
            log.info("Creating sighting for birdId={} and userId={}",
                    createDto.getBirdId(), createDto.getUserId());
            checkIdReferences(createDto);

            ResponseSightingDTO responseDto = sightingMapper.toDto(
                    sightingRepository.save(
                            sightingMapper.toModel(createDto)
                    )
            );

            log.info("Sighting with id={} created", responseDto.getId());

            // If we reach this point we can assume the sighting was persisted
            sightingPublisher.publish(responseDto);

            auditLogger.success("SIGHTING_CREATE", "sightingId=%s birdId=%s userId=%s".formatted(
                    responseDto.getId(), createDto.getBirdId(), createDto.getUserId()
            ));
            return responseDto;
        } catch (RuntimeException ex) {
            auditLogger.failure("SIGHTING_CREATE", "birdId=%s userId=%s".formatted(
                    createDto == null ? null : createDto.getBirdId(),
                    createDto == null ? null : createDto.getUserId()
            ), ex);
            throw ex;
        }
    }

    @Override
    public ResponseSightingDTO getById(long id) {
        return sightingMapper.toDto(
                sightingRepository.findById(id).orElseThrow(
                        () -> new EntityDoesntExistException("Sighting with id %d doesn't exist and can't be fetched."
                                .formatted(id)
                        )
                )
        );
    }

    @Override
    public Page<ResponseSightingDTO> get(QuerySightingDTO queryDto, Pageable pageable) {
        return sightingRepository.findBy(
                SightingSpecificationUtil.buildFromQueryDTO(queryDto),
                p -> p.page(pageable)
        ).map(sightingMapper::toDto);
    }

    @Override
    public ResponseSightingDTO update(long id, CreateUpdateSightingDTO updateDto) {
        try {
            checkIdReferences(updateDto);

            Sighting sighting = sightingRepository.findById(id).orElseThrow(
                    () -> new EntityDoesntExistException("Sighting with id %d doesn't exist and can't be updated"
                        .formatted(id)
                    )
            );

            sightingMapper.update(sighting, updateDto);
            sightingRepository.save(sighting);

            ResponseSightingDTO updatedSighting = sightingMapper.toDto(sighting);
            auditLogger.success("SIGHTING_UPDATE", "sightingId=" + id);
            return updatedSighting;
        } catch (RuntimeException ex) {
            auditLogger.failure("SIGHTING_UPDATE", "sightingId=" + id, ex);
            throw ex;
        }
    }

    @Override
    public ResponseSightingDTO delete(long id) {
        try {
            Sighting sighting = sightingRepository.findById(id).orElseThrow(
                    () -> new EntityDoesntExistException("Sighting with id %d doesn't exist and can't be deleted"
                            .formatted(id)
                    )
            );

            sightingRepository.delete(sighting);

            ResponseSightingDTO deletedSighting = sightingMapper.toDto(sighting);
            auditLogger.success("SIGHTING_DELETE", "sightingId=" + id);
            return deletedSighting;
        } catch (RuntimeException ex) {
            auditLogger.failure("SIGHTING_DELETE", "sightingId=" + id, ex);
            throw ex;
        }
    }

    private void checkIdReferences(CreateUpdateSightingDTO dto) {
        if (!userClient.userExists(dto.getUserId())) {
            throw new ReferredEntityDoesntExistException(
                    "User with id %d doesn't exist".formatted(dto.getUserId())
            );
        }

        if (!birdClient.birdExists(dto.getBirdId())) {
            throw new ReferredEntityDoesntExistException(
                    "Bird with id %d doesn't exist".formatted(dto.getBirdId())
            );
        }
    }

}
