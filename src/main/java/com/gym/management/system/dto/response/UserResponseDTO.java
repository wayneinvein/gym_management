package com.gym.management.system.dto.response;

import com.gym.management.system.enums.UserRoles;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String username;
    private UserRoles userRole;
}
