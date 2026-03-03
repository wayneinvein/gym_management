package com.gym.management.system.service.implememtationclasses;

import com.gym.management.system.entity.User;
import com.gym.management.system.exception.UserNotFoundException;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setUserRole(user.getUserRole());

            return userRepository.save(updatedUser);
        }

        throw new UserNotFoundException("user with id: " + id + " not found");
    }

    @Override
    public User deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userRepository.deleteById(id);
            return user.get();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}