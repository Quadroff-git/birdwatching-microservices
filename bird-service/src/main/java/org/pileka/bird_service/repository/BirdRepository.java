package org.pileka.bird_service.repository;

import org.pileka.bird_service.model.Bird;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BirdRepository extends CrudRepository<Bird, Long>, PagingAndSortingRepository<Bird, Long>, JpaSpecificationExecutor<Bird> { }
