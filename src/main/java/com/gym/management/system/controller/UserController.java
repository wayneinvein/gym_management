package com.gym.management.system.controller;

import com.gym.management.system.entity.User;
import com.gym.management.system.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}