package com.gym.management.system.controller;

import com.gym.management.system.dto.response.AttendanceResponseDTO;
import com.gym.management.system.security.SecurityUtils;
import com.gym.management.system.service.interfaces.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing member attendance.
 *
 * Access control:
 * - ADMIN  → full access
 * - MEMBER → can check in, check out, and view own attendance
 */
@Tag(name = "Attendance APIs", description = "Operations related to member attendance")
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final SecurityUtils securityUtils;

    /**
     * Marks check-in for a member.
     * One check-in per member per day is allowed.
     */
    @Operation(summary = "Member check-in")
    @PostMapping("/checkin/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<AttendanceResponseDTO> checkIn(@PathVariable Long memberId) {
        return new ResponseEntity<>(
                attendanceService.checkIn(memberId),
                HttpStatus.CREATED
        );
    }

    /**
     * Marks check-out for a member.
     * Member must have checked in first.
     */
    @Operation(summary = "Member check-out")
    @PatchMapping("/checkout/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<AttendanceResponseDTO> checkOut(@PathVariable Long memberId) {
        return ResponseEntity.ok(attendanceService.checkOut(memberId));
    }

    /**
     * Returns today's attendance records.
     * Used by admin to see who is currently in the gym.
     */
    @Operation(summary = "Get today's attendance")
    @GetMapping("/today")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponseDTO>> getTodayAttendance() {
        return ResponseEntity.ok(attendanceService.getTodayAttendance());
    }

    /**
     * Returns all attendance records for a specific member.
     */
    @Operation(summary = "Get attendance by member")
    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByMember(
            @PathVariable Long memberId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByMember(memberId));
    }

    /**
     * Returns attendance records for a member within a date range.
     * Date format: yyyy-MM-dd
     */
    @Operation(summary = "Get attendance by date range")
    @GetMapping("/member/{memberId}/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByDateRange(
            @PathVariable Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                attendanceService.getAttendanceByMemberAndDateRange(memberId, startDate, endDate)
        );
    }

    /**
     * Returns attendance records for the currently logged-in member.
     */
    @Operation(summary = "Get my attendance")
    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<AttendanceResponseDTO>> getMyAttendance() {
        String username = securityUtils.getCurrentUsername();
        return ResponseEntity.ok(attendanceService.getMyAttendance(username));
    }
}