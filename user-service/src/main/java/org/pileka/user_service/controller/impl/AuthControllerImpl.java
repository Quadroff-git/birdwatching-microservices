package org.pileka.user_service.controller.impl;

import lombok.RequiredArgsConstructor;
import org.pileka.user_service.controller.AuthController;
import org.pileka.user_service.dto.security.LoginDTO;
import org.pileka.user_service.dto.security.TokenDTO;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseUserDTO register(CreateUpdateUserDTO createDto) {
        return authService.register(createDto);
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
}
