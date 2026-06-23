package org.pileka.user_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pileka.user_service.model.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private boolean enabled;

    private Set<Role> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;
}
