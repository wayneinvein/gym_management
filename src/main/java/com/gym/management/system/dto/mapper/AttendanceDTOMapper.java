package com.gym.management.system.dto.mapper;

import com.gym.management.system.dto.response.AttendanceResponseDTO;
import com.gym.management.system.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * MapStruct mapper for converting Attendance entity to DTOs.
 */
@Mapper(componentModel = "spring")
public interface AttendanceDTOMapper {

    /**
     * Converts Attendance entity to response DTO.
     * Extracts flat fields from nested member object.
     */
    @Mapping(target = "memberId", source = "member.memberId")
    @Mapping(target = "memberName", source = "member.memberName")
    AttendanceResponseDTO toResponse(Attendance attendance);

    // List mapping
    List<AttendanceResponseDTO> toResponse(List<Attendance> attendances);
}