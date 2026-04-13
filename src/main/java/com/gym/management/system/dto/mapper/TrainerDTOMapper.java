package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.TrainerRequestDTO;
import com.gym.management.system.dto.response.TrainerResponseDTO;
import com.gym.management.system.entity.Trainers;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerDTOMapper {

    // Entity → Response DTO
    TrainerResponseDTO toResponse(Trainers trainer);

    // List mapping
    List<TrainerResponseDTO> toResponse(List<Trainers> trainers);

    // Request DTO → Entity
    Trainers toEntity(TrainerRequestDTO dto);
}



