package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MemberRequestDTO;
import com.gym.management.system.dto.response.MemberResponseDTO;
import com.gym.management.system.entity.Members;
import org.mapstruct.Mapper;

//MapStruct will auto-generate conversions
//You just call mapper methods — no manual mapping
@Mapper(componentModel = "spring")
public interface MemberDTOMapper {

    // Convert Request -> Entity
    Members toEntity(MemberRequestDTO request);

    // Convert Entity -> Response
    MemberResponseDTO toResponse(Members member);
}