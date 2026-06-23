package org.pileka.user_service.controller.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.user_service.controller.UserController;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.model.Role;
import org.pileka.user_service.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class UserControllerImpl implements UserController {
    private final UserService userService;


    @Override
    public ResponseUserDTO getUserById(long id) {
        return userService.getById(id);
    }

    @Override
    public Page<ResponseUserDTO> getUsers(Pageable pageable) {
        return userService.get(pageable);
    }

    @Override
    public ResponseUserDTO updateUser(long id, CreateUpdateUserDTO updateDto) {
        return userService.update(id, updateDto);
    }

    @Override
    public ResponseUserDTO updateUserRoles(long id, Set<Role> roles) {
        return userService.updateRoles(id, roles);
    }

    @Override
    public ResponseUserDTO deleteUser(long id) {
        return userService.delete(id);
    }
}
