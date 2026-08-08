package org.pileka.user_service.service;

import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

public interface UserService {

    @PreAuthorize("authentication.name == #id.toString() || hasRole('ADMIN')")
    ResponseUserDTO getById(long id);

    @PreAuthorize("hasRole('ADMIN')")
    Page<ResponseUserDTO> get(Pageable pageable);

    @PreAuthorize("authentication.name == #id.toString() || hasRole('ADMIN')")
    ResponseUserDTO update(long id, CreateUpdateUserDTO updateDto);

    @PreAuthorize("authentication.name == #id.toString() || hasRole('ADMIN')")
    ResponseUserDTO delete(long id);

    @PreAuthorize("hasRole('ADMIN')")
    ResponseUserDTO updateRoles(long id, Set<Role> roles);
}
