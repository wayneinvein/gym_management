package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.UserRequestDTO;
import com.gym.management.system.dto.response.UserResponseDTO;
import com.gym.management.system.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    // Entity → Response DTO
    UserResponseDTO toResponse(User user);

    // List mapping
    List<UserResponseDTO> toResponse(List<User> users);

    // Request DTO → Entity
    User toEntity(UserRequestDTO dto);

}