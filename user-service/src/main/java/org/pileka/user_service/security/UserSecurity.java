package org.pileka.user_service.security;

import lombok.RequiredArgsConstructor;
import org.pileka.user_service.model.User;
import org.pileka.user_service.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Convenience class to use in Method Security expressions
 */
@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;

    /*
    The reason this class was added is that the Authentication instance uses the username as the principal, and when
    all we have for method arguments is the entity's id, we can't check ownership without a db lookup, which is
    exactly what's going on here.
     */
    public boolean hasId(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<User> targetUser = userRepository.findById(userId);

        return targetUser
                .map(User::getUsername)
                .map(username -> username.equals(currentUsername))
                .orElse(false);
    }

}
