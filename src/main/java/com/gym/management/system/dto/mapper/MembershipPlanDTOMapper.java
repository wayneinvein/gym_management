package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MembershipPlanRequestDTO;
import com.gym.management.system.dto.response.MembershipPlanResponseDTO;
import com.gym.management.system.entity.MembershipPlan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MembershipPlanDTOMapper {

    MembershipPlanResponseDTO toResponse(MembershipPlan plan);
    MembershipPlan toEntity(MembershipPlanRequestDTO dto);
    List<MembershipPlanResponseDTO> toResponse(List<MembershipPlan> plans);
}
