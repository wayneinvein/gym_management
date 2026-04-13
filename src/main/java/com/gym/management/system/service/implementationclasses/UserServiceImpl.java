package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.UserDTOMapper;
import com.gym.management.system.dto.request.UserRequestDTO;
import com.gym.management.system.dto.response.UserResponseDTO;
import com.gym.management.system.entity.User;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.UserRepository;
import com.gym.management.system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.gym.management.system.enums.UserRoles.ADMIN;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    @Override
    public UserResponseDTO addUser(UserRequestDTO user) {

        if(user.getUserRole().equals(ADMIN) && userRepository.existsByUserRole(ADMIN)) {
            throw new RuntimeException("Admin already exists. Only one admin allowed.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User newUser = userDTOMapper.toEntity(user);
        return userDTOMapper.toResponse(userRepository.save(newUser));

    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO  user, Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("user with id: " + id + " not found"));

        if(existingUser.getUserRole().equals(ADMIN)) {
            throw new RuntimeException("Admin cannot be modified");
        }
        if(user.getUserRole().equals(ADMIN) && userRepository.existsByUserRole(ADMIN)) {
            throw new RuntimeException("Admin already exists. Only one admin allowed.");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setUserRole(user.getUserRole());

        return userDTOMapper.toResponse(userRepository.save(existingUser));
    }

    @Override
    public UserResponseDTO deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("user with id: " + id + " not found"));


        if(user.getUserRole().equals(ADMIN)) {
            throw new RuntimeException("Admin cannot be deleted");
        }

        userRepository.delete(user);

        return userDTOMapper.toResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userDTOMapper.toResponse(userRepository.findAll());

    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        return userRepository.findById(id)
                .map(userDTOMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}