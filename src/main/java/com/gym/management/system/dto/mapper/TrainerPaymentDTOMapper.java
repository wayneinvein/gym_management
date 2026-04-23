package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.response.TrainerPaymentResponseDTO;
import com.gym.management.system.entity.TrainerPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * MapStruct mapper for converting TrainerPayment entity to DTOs.
 */
@Mapper(componentModel = "spring")
public interface TrainerPaymentDTOMapper {

    /**
     * Converts TrainerPayment entity to response DTO.
     * Extracts flat fields from nested trainer object.
     */
    @Mapping(target = "trainerId", source = "trainer.trainerId")
    @Mapping(target = "trainerName", source = "trainer.trainerName")
    TrainerPaymentResponseDTO toResponse(TrainerPayment payment);

    // List mapping
    List<TrainerPaymentResponseDTO> toResponse(List<TrainerPayment> payments);
}