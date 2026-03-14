package com.gym.management.system.config;

import com.gym.management.system.entity.User;
import com.gym.management.system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.gym.management.system.enums.UserRoles.ADMIN;

@Component
public class AdminSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdmin() {

        System.out.println("Seeder running...");

        if(!userRepository.existsByUserRole(ADMIN)) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setUserRole(ADMIN);

            userRepository.save(admin);
        }
}
}