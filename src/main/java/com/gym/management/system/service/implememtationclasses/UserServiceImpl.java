package com.gym.management.system.service.implememtationclasses;

import com.gym.management.system.entity.User;
import com.gym.management.system.enums.UserRoles;
import com.gym.management.system.exception.UserNotFoundException;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.gym.management.system.enums.UserRoles.MEMBER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {

        //if ADMIN already present throw an exception
        if(userRepository.existsByUserRole(UserRoles.ADMIN)){
            throw new RuntimeException("Admin already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("user with id: " + id + " not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setUserRole(user.getUserRole());

        return userRepository.save(existingUser);
    }

    @Override
    public User deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("user with id: " + id + " not found"));

        userRepository.delete(user);

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id: " + id + " not found"));
    }
}