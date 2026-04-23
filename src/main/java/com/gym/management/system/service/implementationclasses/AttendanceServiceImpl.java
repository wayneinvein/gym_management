package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.AttendanceDTOMapper;
import com.gym.management.system.dto.response.AttendanceResponseDTO;
import com.gym.management.system.entity.Attendance;
import com.gym.management.system.entity.Member;
import com.gym.management.system.exception.InvalidInputException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.AttendanceRepository;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.service.interfaces.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service implementation for managing member attendance.
 *
 * Handles check-in, check-out, and attendance retrieval.
 * One check-in per member per day is enforced.
 */
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final AttendanceDTOMapper attendanceDTOMapper;

    /**
     * Marks check-in for a member.
     *
     * Creates a new attendance record with current time as check-in time.
     * Throws InvalidInputException if member already checked in today.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    public AttendanceResponseDTO checkIn(Long memberId) {

        // Fetch member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + memberId));

        LocalDate today = LocalDate.now();

        // Prevent duplicate check-in on the same day
        attendanceRepository.findByMemberMemberIdAndDate(memberId, today)
                .ifPresent(existing -> {
                    throw new InvalidInputException("Member has already checked in today");
                });

        // Create new attendance record
        Attendance attendance = new Attendance();
        attendance.setMember(member);
        attendance.setDate(today);
        attendance.setCheckInTime(LocalTime.now()); // current time as check-in
        attendance.setCheckOutTime(null);            // not checked out yet

        return attendanceDTOMapper.toResponse(attendanceRepository.save(attendance));
    }

    /**
     * Marks check-out for a member.
     *
     * Updates the existing attendance record with current time as check-out.
     * Throws NotFoundException if member hasn't checked in today.
     * Throws InvalidInputException if member already checked out.
     */
    @Override
    public AttendanceResponseDTO checkOut(Long memberId) {

        // Member must exist
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member not found with id: " + memberId);
        }

        LocalDate today = LocalDate.now();

        // Find today's attendance record — must have checked in first
        Attendance attendance = attendanceRepository
                .findByMemberMemberIdAndDate(memberId, today)
                .orElseThrow(() -> new NotFoundException("No check-in found for member today"));

        // Prevent duplicate check-out
        if (attendance.getCheckOutTime() != null) {
            throw new InvalidInputException("Member has already checked out today");
        }

        // Set check-out time to current time
        attendance.setCheckOutTime(LocalTime.now());

        return attendanceDTOMapper.toResponse(attendanceRepository.save(attendance));
    }

    /**
     * Returns all attendance records for a specific member.
     * Throws NotFoundException if member does not exist.
     */
    @Override
    public List<AttendanceResponseDTO> getAttendanceByMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member not found with id: " + memberId);
        }

        return attendanceDTOMapper.toResponse(
                attendanceRepository.findByMemberMemberId(memberId)
        );
    }

    /**
     * Returns all attendance records for today.
     * Used by admin to see who is currently in the gym.
     */
    @Override
    public List<AttendanceResponseDTO> getTodayAttendance() {
        return attendanceDTOMapper.toResponse(
                attendanceRepository.findByDate(LocalDate.now())
        );
    }

    /**
     * Returns attendance records for a member within a date range.
     * Used for monthly or weekly attendance reports.
     */
    @Override
    public List<AttendanceResponseDTO> getAttendanceByMemberAndDateRange(
            Long memberId, LocalDate startDate, LocalDate endDate) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("Member not found with id: " + memberId);
        }

        // Validate date range
        if (startDate.isAfter(endDate)) {
            throw new InvalidInputException("Start date cannot be after end date");
        }

        return attendanceDTOMapper.toResponse(
                attendanceRepository.findByMemberMemberIdAndDateBetween(memberId, startDate, endDate)
        );
    }

    /**
     * Returns attendance records for the currently logged-in member.
     * Reads username from security context passed from controller.
     */
    @Override
    public List<AttendanceResponseDTO> getMyAttendance(String username) {

        // Find member linked to this login account
        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new NotFoundException("Member profile not found"));

        return attendanceDTOMapper.toResponse(
                attendanceRepository.findByMemberMemberId(member.getMemberId())
        );
    }
}