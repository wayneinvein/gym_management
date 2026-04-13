package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.request.UserRequestDTO;
import com.gym.management.system.dto.response.UserResponseDTO;
import com.gym.management.system.entity.User;

import java.util.List;

public interface UserService {

   public UserResponseDTO addUser(UserRequestDTO user);
   public UserResponseDTO  updateUser(UserRequestDTO  user, Long id);
   public UserResponseDTO  deleteUser(Long id);
   public List<UserResponseDTO> getAllUsers();
   public UserResponseDTO  getUserById(Long id);

}
