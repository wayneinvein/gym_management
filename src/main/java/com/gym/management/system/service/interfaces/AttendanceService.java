package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.response.AttendanceResponseDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing member attendance.
 */
public interface AttendanceService {

    // Mark check-in for a member
    AttendanceResponseDTO checkIn(Long memberId);

    // Mark check-out for a member
    AttendanceResponseDTO checkOut(Long memberId);

    // Get all attendance records for a specific member
    List<AttendanceResponseDTO> getAttendanceByMember(Long memberId);

    // Get today's attendance records
    List<AttendanceResponseDTO> getTodayAttendance();

    // Get attendance records for a member within a date range
    List<AttendanceResponseDTO> getAttendanceByMemberAndDateRange(
            Long memberId, LocalDate startDate, LocalDate endDate);

    // Get attendance for logged-in member
    List<AttendanceResponseDTO> getMyAttendance(String username);
}