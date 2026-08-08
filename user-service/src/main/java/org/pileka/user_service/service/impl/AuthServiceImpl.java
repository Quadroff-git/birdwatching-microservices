package org.pileka.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pileka.user_service.dto.security.LoginDTO;
import org.pileka.user_service.dto.security.TokenDTO;
import org.pileka.user_service.dto.user.CreateUpdateUserDTO;
import org.pileka.user_service.dto.user.ResponseUserDTO;
import org.pileka.user_service.mapper.UserMapper;
import org.pileka.user_service.model.Role;
import org.pileka.user_service.model.User;
import org.pileka.user_service.repository.UserRepository;
import org.pileka.birdwatching_security_commons.jwt.JWTProvider;
import org.pileka.user_service.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Log log = LogFactory.getLog(AuthServiceImpl.class);
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTProvider jwtProvider;

    @Override
    public ResponseUserDTO register(CreateUpdateUserDTO createDto) {
        return userMapper.toDto(
                userRepository.save(
                        userMapper.toModel(createDto, passwordEncoder)
                )
        );
    }

    @Override
    public TokenDTO login(LoginDTO loginDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        User authenticatedUser = userRepository.findByUsername(auth.getName()).get();

        return new TokenDTO(
                jwtProvider.generateBearerToken(
                    authenticatedUser.getId().toString(),
                    authenticatedUser.getRoles()
                            .stream()
                            .map(Role::name)
                            .collect(Collectors.toSet())
                ),
                jwtProvider.getBearerTokenExpiration()
        );
    }
}
