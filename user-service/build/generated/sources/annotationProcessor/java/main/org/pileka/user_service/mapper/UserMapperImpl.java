package org.pileka.user_service.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.model.Role;
import org.pileka.user_service.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-17T14:12:17+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.5.1.jar, environment: Java 17.0.19 (Ubuntu)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public ResponseUserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        ResponseUserDTO responseUserDTO = new ResponseUserDTO();

        responseUserDTO.setId( user.getId() );
        responseUserDTO.setUsername( user.getUsername() );
        responseUserDTO.setEmail( user.getEmail() );
        responseUserDTO.setFirstName( user.getFirstName() );
        responseUserDTO.setLastName( user.getLastName() );
        responseUserDTO.setEnabled( user.isEnabled() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            responseUserDTO.setRoles( new LinkedHashSet<Role>( set ) );
        }
        responseUserDTO.setCreatedAt( user.getCreatedAt() );
        responseUserDTO.setUpdatedAt( user.getUpdatedAt() );
        responseUserDTO.setLastLogin( user.getLastLogin() );

        return responseUserDTO;
    }

    @Override
    public User toModel(CreateUpdateUserDTO dto, PasswordEncoder passwordEncoder) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( dto.getUsername() );
        user.email( dto.getEmail() );
        user.firstName( dto.getFirstName() );
        user.lastName( dto.getLastName() );

        user.enabled( true );

        User userResult = user.build();

        encodePassword( dto, userResult, passwordEncoder );

        return userResult;
    }

    @Override
    public void update(User user, CreateUpdateUserDTO dto) {
        if ( dto == null ) {
            return;
        }

        user.setUsername( dto.getUsername() );
        user.setEmail( dto.getEmail() );
        user.setPassword( dto.getPassword() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
    }
}
