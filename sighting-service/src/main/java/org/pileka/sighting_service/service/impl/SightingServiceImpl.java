package org.pileka.sighting_service.service.impl;

import lombok.RequiredArgsConstructor;
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

@Component
@RequiredArgsConstructor
public class SightingServiceImpl implements SightingService {

    private final SightingRepository sightingRepository;
    private final SightingMapper sightingMapper;
    private final BirdClient birdClient;
    private final UserClient userClient;
    private final SightingPublisher sightingPublisher;

    @Override
    public ResponseSightingDTO create(CreateUpdateSightingDTO createDto) {
        checkIdReferences(createDto);

        ResponseSightingDTO responseDto = sightingMapper.toDto(
                sightingRepository.save(
                        sightingMapper.toModel(createDto)
                )
        );

        // If we reach this point we can assume the sighting was persisted
        sightingPublisher.publish(responseDto);
        return responseDto;
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
        checkIdReferences(updateDto);

        Sighting sighting = sightingRepository.findById(id).orElseThrow(
                () -> new EntityDoesntExistException("Sighting with id %d doesn't exist and can't be updated"
                    .formatted(id)
                )
        );

        sightingMapper.update(sighting, updateDto);
        sightingRepository.save(sighting);
        return sightingMapper.toDto(sighting);
    }

    @Override
    public ResponseSightingDTO delete(long id) {
        Sighting sighting = sightingRepository.findById(id).orElseThrow(
                () -> new EntityDoesntExistException("Sighting with id %d doesn't exist and can't be deleted"
                        .formatted(id)
                )
        );

        sightingRepository.delete(sighting);
        return sightingMapper.toDto(sighting);
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
