package org.pileka.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.birdwatching_exception_commons.exception.EntityDoesntExistException;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.mapper.UserMapper;
import org.pileka.user_service.model.Role;
import org.pileka.user_service.repository.UserRepository;
import org.pileka.user_service.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public ResponseUserDTO getById(long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesntExistException("User with id %d doesn't exist and can't be fetched.".formatted(id))));
    }

    @Override
    public Page<ResponseUserDTO> get(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public ResponseUserDTO update(long id, CreateUpdateUserDTO updateDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesntExistException("User with id %d doesn't exist and can't be updated.".formatted(id)));

        userMapper.update(user, updateDto);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public ResponseUserDTO delete(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesntExistException("User with id %d doesn't exist and can't be deleted.".formatted(id)));

        userRepository.delete(user);
        return userMapper.toDto(user);
    }

    @Override
    public ResponseUserDTO updateRoles(long id, Set<Role> roles) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityDoesntExistException("User with id %d doesn't exist and can't be updated.".formatted(id)));

        user.setRoles(roles);
        userRepository.save(user);

        return userMapper.toDto(user);
    }
}
