package org.pileka.user_service.mapper;

import org.mapstruct.*;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    public abstract ResponseUserDTO toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", expression = "java(true)")
    public abstract User toModel(CreateUpdateUserDTO dto, @Context PasswordEncoder passwordEncoder);

    public abstract void update(@MappingTarget User user, CreateUpdateUserDTO dto);

    @AfterMapping
    protected void encodePassword(CreateUpdateUserDTO dto, @MappingTarget User user,  @Context PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

}
