package com.gym.management.system.dto.request;

import com.gym.management.system.enums.UserRoles;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private UserRoles userRole;
}
