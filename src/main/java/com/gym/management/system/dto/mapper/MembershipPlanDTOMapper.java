package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MembershipPlanRequestDTO;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Maps MembershipPlan entity <-> DTOs
 */
@Mapper(componentModel = "spring")
public interface MembershipPlanDTOMapper {

    /**
     * Convert entity to response DTO
     */
    MembershipPlanResponseDTO toResponse(MembershipPlan plan);

    /**
     * Convert list of entity to list of response DTO
     */
    List<MembershipPlanResponseDTO> toResponse(List<MembershipPlan> plan);

    /**
     * Convert request DTO to entity
     */
    MembershipPlan toEntity(MembershipPlanRequestDTO dto);

    /**
     * Convert list of entities to list of response DTOs
     */
    List<MembershipPlanResponseDTO> toResponseList(List<MembershipPlan> plans);
}