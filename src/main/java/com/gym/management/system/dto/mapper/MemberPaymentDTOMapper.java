package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.entity.MemberPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for converting MemberPayment entity to DTOs.
 *
 * Request DTO is not mapped here because payment creation
 * involves multiple entities — handled manually in service.
 */
@Mapper(componentModel = "spring")
public interface MemberPaymentDTOMapper {

    /**
     * Converts MemberPayment entity to response DTO.
     * Extracts flat fields from nested member and membership objects.
     */
    @Mapping(target = "memberId", source = "member.memberId")
    @Mapping(target = "memberName", source = "member.memberName")
    @Mapping(target = "membershipId", source = "membership.membershipId")
    @Mapping(target = "planName", source = "membership.plan.name")
    MemberPaymentResponseDTO toResponse(MemberPayment payment);

    // List mapping
    List<MemberPaymentResponseDTO> toResponse(List<MemberPayment> payments);
}