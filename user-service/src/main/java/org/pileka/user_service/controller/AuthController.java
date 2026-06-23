package org.pileka.user_service.controller;

import jakarta.validation.Valid;
import org.pileka.user_service.dto.security.LoginDTO;
import org.pileka.user_service.dto.security.TokenDTO;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/auth")
public interface AuthController {

    @PostMapping("/register")
    ResponseUserDTO register(@Valid @RequestBody CreateUpdateUserDTO createDto);

    @PostMapping("/login")
    TokenDTO login(@Valid @RequestBody LoginDTO loginDTO);

}
