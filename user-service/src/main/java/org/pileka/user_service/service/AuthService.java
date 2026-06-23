package org.pileka.user_service.service;

import org.pileka.user_service.dto.security.TokenDTO;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.security.LoginDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;

/**
 * Service-layer authorization component
 */
public interface AuthService {

    ResponseUserDTO register(CreateUpdateUserDTO createDto);

    TokenDTO login(LoginDTO loginDto);

}
