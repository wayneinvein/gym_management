package com.gym.management.system.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for returning attendance details in API responses.
 *
 * Uses flat fields instead of full entity objects
 * to keep the response clean and avoid circular references.
 */
@Data
public class AttendanceResponseDTO {

    // Unique identifier of the attendance record
    private Long attendanceId;

    // Member details — flat fields
    private Long memberId;
    private String memberName;

    // Date of attendance
    private LocalDate date;

    // Check in time
    private LocalTime checkInTime;

    // Check out time — null if member hasn't checked out yet
    private LocalTime checkOutTime;

    // When this record was created
    private LocalDateTime createdAt;
}