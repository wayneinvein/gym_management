package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.entity.Membership;
import org.mapstruct.Mapper;

// MapStruct will auto-generate implementation
@Mapper(componentModel = "spring")
public interface MembershipDTOMapper {

    //converting requestDTO into entity
    Membership toEntity(MembershipRequestDTO membershipRequestDto);

    //converting entity to responseDTO so that it can be sent
    MembershipResponseDTO toResponse(Membership membership);

}
