package org.pileka.user_service.controller;

import jakarta.validation.Valid;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("${api.paths.users}")
public interface UserController {

    @GetMapping(path = "/{id}", produces = "application/json")
    ResponseUserDTO getUserById(@PathVariable long id);

    @GetMapping(path = "/", produces = "application/json")
    Page<ResponseUserDTO> getUsers(@PageableDefault(size = 20, sort = "username") Pageable pageable);

    @PutMapping(path = "/{id}", produces = "application/json")
    ResponseUserDTO updateUser(@PathVariable long id, @Valid @RequestBody CreateUpdateUserDTO updateDto);

    @PutMapping(path = "/{id}/roles", produces = "application/json")
    ResponseUserDTO updateUserRoles(@PathVariable long id, @RequestBody Set<Role> roles);

    @DeleteMapping(path = "/{id}")
    ResponseUserDTO deleteUser(@PathVariable long id);
}
