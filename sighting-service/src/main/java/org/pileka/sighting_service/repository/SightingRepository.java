package org.pileka.sighting_service.repository;

import org.pileka.sighting_service.model.Sighting;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface SightingRepository extends CrudRepository<Sighting, Long>, JpaSpecificationExecutor<Sighting> {}
