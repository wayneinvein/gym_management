package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MemberRequestDto;
import com.gym.management.system.dto.response.MemberResponseDto;
import com.gym.management.system.entity.Members;
import org.mapstruct.Mapper;

//MapStruct will auto-generate conversions
//You just call mapper methods — no manual mapping
@Mapper(componentModel = "spring")
public interface MemberDtoMapper {

    // Convert Request -> Entity
    Members toEntity(MemberRequestDto request);

    // Convert Entity -> Response
    MemberResponseDto toResponse(Members member);
}