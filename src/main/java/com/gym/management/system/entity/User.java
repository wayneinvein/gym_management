package com.gym.management.system.entity;

import com.gym.management.system.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing an application user.
 * Stores authentication credentials and role-based access info.
 */
@Data
@Entity
public class User {

    // Primary key (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique username used for login
    @Column(unique = true)
    private String username;

    // Encrypted password (should be stored using password encoder)
    private String password;

    // Role assigned to the user (e.g., ADMIN, TRAINER, RECEPTIONIST)
    @Enumerated(EnumType.STRING)
    private UserRoles userRole;
}