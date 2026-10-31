package org.pileka.sighting_service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pileka.birdwatching_audit_commons.audit.AuditLogger;
import org.pileka.birdwatching_exception_commons.exception.EntityDoesntExistException;
import org.pileka.birdwatching_exception_commons.exception.ReferredEntityDoesntExistException;
import org.pileka.sighting_service.client.BirdClient;
import org.pileka.sighting_service.client.UserClient;
import org.pileka.sighting_service.dto.CreateUpdateSightingDTO;
import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.pileka.sighting_service.mapper.SightingMapper;
import org.pileka.sighting_service.model.Sighting;
import org.pileka.sighting_service.logging.PerformanceMonitor;
import org.pileka.sighting_service.publisher.SightingPublisher;
import org.pileka.sighting_service.repository.SightingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SightingServiceImplTest {

    private SightingRepository sightingRepository;

    private SightingMapper sightingMapper;

    private BirdClient birdClient;

    private UserClient userClient;

    private SightingPublisher sightingPublisher;

    private AuditLogger auditLogger;

    private PerformanceMonitor performanceMonitor;

    private SightingServiceImpl sightingService;

    @BeforeEach
    void setUp() {
        sightingRepository = mock(SightingRepository.class);
        sightingMapper = mock(SightingMapper.class);
        birdClient = mock(BirdClient.class);
        userClient = mock(UserClient.class);
        sightingPublisher = mock(SightingPublisher.class);
        auditLogger = mock(AuditLogger.class);
        performanceMonitor = new PerformanceMonitor(mock(org.slf4j.Logger.class), 1000);
        sightingService = new SightingServiceImpl(
                sightingRepository, sightingMapper, birdClient, userClient, sightingPublisher, auditLogger,
                performanceMonitor
        );
    }

    @Test
    void createAuditsSuccess() {
        CreateUpdateSightingDTO createDto = createDto();
        Sighting sighting = new Sighting();
        sighting.setId(10L);
        ResponseSightingDTO response = response(10L);

        when(userClient.userExists(2L)).thenReturn(true);
        when(birdClient.birdExists(1L)).thenReturn(true);
        when(sightingMapper.toModel(createDto)).thenReturn(sighting);
        when(sightingRepository.save(sighting)).thenReturn(sighting);
        when(sightingMapper.toDto(sighting)).thenReturn(response);

        assertThat(sightingService.create(createDto)).isSameAs(response);

        verify(auditLogger).success("SIGHTING_CREATE", "sightingId=10 birdId=1 userId=2");
        verify(sightingPublisher).publish(response);
    }

    @Test
    void createAuditsFailureWhenUserDoesNotExist() {
        CreateUpdateSightingDTO createDto = createDto();

        when(userClient.userExists(2L)).thenReturn(false);

        assertThatThrownBy(() -> sightingService.create(createDto))
                .isInstanceOf(ReferredEntityDoesntExistException.class);

        verify(auditLogger).failure(
                eq("SIGHTING_CREATE"), eq("birdId=1 userId=2"),
                any(ReferredEntityDoesntExistException.class)
        );
        verifyNoInteractions(sightingPublisher);
    }

    @Test
    void updateAuditsSuccess() {
        CreateUpdateSightingDTO updateDto = createDto();
        Sighting sighting = new Sighting();
        sighting.setId(5L);
        ResponseSightingDTO response = response(5L);

        when(userClient.userExists(2L)).thenReturn(true);
        when(birdClient.birdExists(1L)).thenReturn(true);
        when(sightingRepository.findById(5L)).thenReturn(Optional.of(sighting));
        when(sightingMapper.toDto(sighting)).thenReturn(response);

        assertThat(sightingService.update(5L, updateDto)).isSameAs(response);

        verify(sightingMapper).update(sighting, updateDto);
        verify(sightingRepository).save(sighting);
        verify(auditLogger).success("SIGHTING_UPDATE", "sightingId=5");
    }

    @Test
    void deleteAuditsSuccess() {
        Sighting sighting = new Sighting();
        sighting.setId(5L);
        ResponseSightingDTO response = response(5L);

        when(sightingRepository.findById(5L)).thenReturn(Optional.of(sighting));
        when(sightingMapper.toDto(sighting)).thenReturn(response);

        assertThat(sightingService.delete(5L)).isSameAs(response);

        verify(sightingRepository).delete(sighting);
        verify(auditLogger).success("SIGHTING_DELETE", "sightingId=5");
    }

    @Test
    void deleteAuditsFailureWhenSightingDoesNotExist() {
        when(sightingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sightingService.delete(99L))
                .isInstanceOf(EntityDoesntExistException.class);

        verify(auditLogger).failure(
                eq("SIGHTING_DELETE"), eq("sightingId=99"), any(EntityDoesntExistException.class)
        );
        verify(sightingRepository, never()).delete(any(Sighting.class));
    }

    private CreateUpdateSightingDTO createDto() {
        return new CreateUpdateSightingDTO(1L, 2L, LocalDateTime.now(), null);
    }

    private ResponseSightingDTO response(long id) {
        return new ResponseSightingDTO(id, 1L, 2L, LocalDateTime.now(), null, null);
    }
}
