package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MemberRequest;
import com.gym.management.system.dto.response.MemberResponse;
import com.gym.management.system.entity.Members;
import org.mapstruct.Mapper;

//MapStruct will auto-generate conversions
//You just call mapper methods — no manual mapping
@Mapper(componentModel = "spring")
public interface MemberMapper {

    // Convert Request -> Entity
    Members toEntity(MemberRequest request);

    // Convert Entity -> Response
    MemberResponse toResponse(Members member);
}