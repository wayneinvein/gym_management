package com.gym.management.system.service.interfaces;

import com.gym.management.system.entity.User;

import java.util.List;

public interface UserService {

   public User addUser(User user);
   public User updateUser(User user, Long id);
   public User deleteUser(Long id);
   public List<User> getAllUsers();
   public User getUserById(Long id);

}
