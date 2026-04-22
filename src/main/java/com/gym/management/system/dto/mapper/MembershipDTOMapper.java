package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.entity.Membership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Membership entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface MembershipDTOMapper {

    /**
     * Converts Membership entity to MembershipResponseDTO.
     * Extracts flat fields from nested member and plan objects.
     */
    @Mapping(target = "memberId", source = "member.memberId")
    @Mapping(target = "memberName", source = "member.memberName")
    @Mapping(target = "planId", source = "plan.planId")
    @Mapping(target = "planName", source = "plan.name")
    MembershipResponseDTO toResponse(Membership membership);
}