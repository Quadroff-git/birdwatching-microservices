package org.pileka.user_service.repository;

import org.pileka.user_service.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
