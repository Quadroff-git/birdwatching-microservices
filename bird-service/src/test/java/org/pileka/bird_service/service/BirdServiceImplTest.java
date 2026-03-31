package org.pileka.bird_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pileka.bird_service.dto.CreateUpdateBirdDTO;
import org.pileka.bird_service.dto.QueryBirdDTO;
import org.pileka.bird_service.dto.ResponseBirdDTO;
import org.pileka.bird_service.exception.EntityDoesntExistException;
import org.pileka.bird_service.mapper.BirdMapper;
import org.pileka.bird_service.model.Bird;
import org.pileka.bird_service.model.ConservationStatus;
import org.pileka.bird_service.repository.BirdRepository;
import org.pileka.bird_service.service.impl.BirdServiceImpl;
import org.pileka.bird_service.test_util.BirdTestUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BirdServiceImplTest {

    BirdRepository birdRepository;

    BirdServiceImpl birdService;

    BirdServiceImplTest() {
        birdRepository = mock(BirdRepository.class);
        birdService = new BirdServiceImpl(birdRepository, Mappers.getMapper(BirdMapper.class));
    }

    @Test
    void createCreates() {
        CreateUpdateBirdDTO createDto = BirdTestUtil.getCreateUpdateBirdDto();

        when(birdRepository.save(any(Bird.class))).thenReturn(BirdTestUtil.getBird());

        BirdTestUtil.assertDtoEqualsModel(birdService.create(createDto), BirdTestUtil.getBird());

        verify(birdRepository).save(any(Bird.class));
    }

    @Test
    void getByIdGets() {
        Long birdId = 1L;
        Bird expectedBird = BirdTestUtil.getBird();

        when(birdRepository.findById(birdId)).thenReturn(Optional.of(expectedBird));

        BirdTestUtil.assertDtoEqualsModel(birdService.getById(birdId), expectedBird);

        verify(birdRepository, times(1)).findById(birdId);
    }

    @Test
    void getByIdThrowsEntityDoesntExistException() {
        Long nonExistentId = 999L;

        when(birdRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityDoesntExistException.class, () -> {
            birdService.getById(nonExistentId);
        });

        verify(birdRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void getGets() {
        Pageable pageable = PageRequest.of(0, 20);
        QueryBirdDTO queryDto = new QueryBirdDTO();
        List<Bird> birds = List.of(BirdTestUtil.getBird());
        Page<Bird> birdPage = new PageImpl<>(birds, pageable, birds.size());

        when(birdRepository.findBy(
                        Mockito.<PredicateSpecification<Bird>>any(),
                        Mockito.<Function<JpaSpecificationExecutor.SpecificationFluentQuery<Bird>, Page<Bird>>>any())
        ).thenReturn(birdPage);

        Page<ResponseBirdDTO> result = birdService.get(queryDto, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        BirdTestUtil.assertDtoEqualsModel(result.getContent().get(0), birds.get(0));

        verify(birdRepository).findBy(
                Mockito.<PredicateSpecification<Bird>>any(),
                Mockito.<Function<JpaSpecificationExecutor.SpecificationFluentQuery<Bird>, Page<Bird>>>any()
        );
    }

    @Test
    void getGetsEmptyPageWhenNoBirdsExist() {
        Pageable pageable = PageRequest.of(0, 20);
        QueryBirdDTO queryDto = new QueryBirdDTO();
        Page<Bird> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(birdRepository.findBy(
                        Mockito.<PredicateSpecification<Bird>>any(),
                        Mockito.<Function<JpaSpecificationExecutor.SpecificationFluentQuery<Bird>, Page<Bird>>>any())
        ).thenReturn(emptyPage);;

        Page<ResponseBirdDTO> result = birdService.get(queryDto, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());

        verify(birdRepository).findBy(
                Mockito.<PredicateSpecification<Bird>>any(),
                Mockito.<Function<JpaSpecificationExecutor.SpecificationFluentQuery<Bird>, Page<Bird>>>any()
        );
    }

    @Test
    void getGetsWithCustomPageable() {
        Pageable pageable = PageRequest.of(2, 10);
        QueryBirdDTO queryDto = new QueryBirdDTO();
        queryDto.setFamily("Falconidae");
        List<Bird> birds = List.of(BirdTestUtil.getBird());
        Page<Bird> birdPage = new PageImpl<>(birds, pageable, 21);

        when(birdRepository.findBy(
                        Mockito.<PredicateSpecification<Bird>>any(),
                        Mockito.<Function<JpaSpecificationExecutor.SpecificationFluentQuery<Bird>, Page<Bird>>>any())
        ).thenReturn(birdPage);;

        Page<ResponseBirdDTO> result = birdService.get(queryDto, pageable);

        assertNotNull(result);
        assertEquals(21, result.getTotalElements());
        assertEquals(2, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getContent().size());

        verify(birdRepository).findBy(
                Mockito.<PredicateSpecification<Bird>>any(),
                Mockito.<Function<JpaSpecificationExecutor.SpecificationFluentQuery<Bird>, Page<Bird>>>any()
        );
    }

    @Test
    void updateUpdates() {
        Long birdId = 1L;
        CreateUpdateBirdDTO updateDto = BirdTestUtil.getCreateUpdateBirdDto();
        Bird existingBird = BirdTestUtil.getBird();
        Bird updatedBird = BirdTestUtil.getBird();

        when(birdRepository.findById(birdId)).thenReturn(Optional.of(existingBird));
        when(birdRepository.save(any(Bird.class))).thenReturn(updatedBird);

        ResponseBirdDTO result = birdService.update(birdId, updateDto);

        BirdTestUtil.assertDtoEqualsModel(result, updatedBird);
        verify(birdRepository, times(1)).findById(birdId);
        verify(birdRepository, times(1)).save(any(Bird.class));
    }

    @Test
    void updateUpdatesWithPartialData() {
        Long birdId = 1L;
        Bird existingBird = BirdTestUtil.getBird();

        // Create DTO with only some fields updated
        CreateUpdateBirdDTO partialUpdateDto = new CreateUpdateBirdDTO();
        partialUpdateDto.setCommonName("Updated Falcon Name");
        partialUpdateDto.setDiet("Updated diet: Small birds and insects");

        when(birdRepository.findById(birdId)).thenReturn(Optional.of(existingBird));
        when(birdRepository.save(any(Bird.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseBirdDTO result = birdService.update(birdId, partialUpdateDto);

        // Verify only the provided fields were updated
        assertEquals("Updated Falcon Name", result.getCommonName());
        assertEquals("Updated diet: Small birds and insects", result.getDiet());
        // Verify other fields remained unchanged
        assertEquals(existingBird.getScientificName(), result.getScientificName());
        assertEquals(existingBird.getFamily(), result.getFamily());
        assertEquals(existingBird.getConservationStatus(), result.getConservationStatus());

        verify(birdRepository, times(1)).findById(birdId);
        verify(birdRepository, times(1)).save(any(Bird.class));
    }

    @Test
    void updateThrowsEntityDoesntExistException() {
        Long nonExistentId = 999L;
        CreateUpdateBirdDTO updateDto = BirdTestUtil.getCreateUpdateBirdDto();

        when(birdRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityDoesntExistException.class, () -> {
            birdService.update(nonExistentId, updateDto);
        });

        verify(birdRepository, times(1)).findById(nonExistentId);
        verify(birdRepository, never()).save(any(Bird.class));
    }

    @Test
    void deleteDeletes() {
        Long birdId = 1L;
        Bird existingBird = BirdTestUtil.getBird();

        when(birdRepository.findById(birdId)).thenReturn(Optional.of(existingBird));
        doNothing().when(birdRepository).delete(existingBird);

        assertDoesNotThrow(() -> birdService.delete(birdId));

        verify(birdRepository, times(1)).findById(birdId);
        verify(birdRepository, times(1)).delete(existingBird);
    }

    @Test
    void deleteThrowsEntityDoesntExistException() {
        Long nonExistentId = 999L;

        when(birdRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityDoesntExistException.class, () -> {
            birdService.delete(nonExistentId);
        });

        verify(birdRepository, times(1)).findById(nonExistentId);
        verify(birdRepository, never()).delete(any(Bird.class));
    }

    @Test
    void createCreatesWithNullOptionalFields() {
        CreateUpdateBirdDTO minimalDto = new CreateUpdateBirdDTO();
        minimalDto.setCommonName("Minimal Bird");
        minimalDto.setScientificName("Minimus avis");
        minimalDto.setConservationStatus(ConservationStatus.DATA_DEFICIENT);
        minimalDto.setAverageWingspan(50.0);
        minimalDto.setAverageWeight(200.0);

        Bird savedBird = new Bird();
        savedBird.setId(2L);
        savedBird.setCommonName(minimalDto.getCommonName());
        savedBird.setScientificName(minimalDto.getScientificName());
        savedBird.setConservationStatus(minimalDto.getConservationStatus());
        savedBird.setAverageWingspan(minimalDto.getAverageWingspan());
        savedBird.setAverageWeight(minimalDto.getAverageWeight());

        when(birdRepository.save(any(Bird.class))).thenReturn(savedBird);

        ResponseBirdDTO result = birdService.create(minimalDto);

        assertNotNull(result);
        assertEquals(minimalDto.getCommonName(), result.getCommonName());
        assertEquals(minimalDto.getScientificName(), result.getScientificName());
        assertNull(result.getFamily());
        assertNull(result.getTypicalHabitat());
        assertNull(result.getDescription());
        assertNull(result.getImageUrl());
        assertNull(result.getDiet());

        verify(birdRepository, times(1)).save(any(Bird.class));
    }

}
