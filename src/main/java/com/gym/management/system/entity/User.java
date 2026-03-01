package com.gym.management.system.entity;

import com.gym.management.system.enums.UserRoles;
import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoles userRole;
}
