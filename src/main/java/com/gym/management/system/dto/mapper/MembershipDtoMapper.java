package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MembershipRequestDto;
import com.gym.management.system.dto.response.MembershipResponseDto;
import com.gym.management.system.entity.Membership;
import org.mapstruct.Mapper;

// MapStruct will auto-generate implementation
@Mapper(componentModel = "spring")
public interface MembershipDtoMapper {

    //converting requestDTO into entity
    Membership toEntity(MembershipRequestDto membershipRequestDto);

    //converting entity to responseDTO so that it can be sent
    MembershipResponseDto toResponse(Membership membership);

}
