package com.gym.management.system.repository;

import com.gym.management.system.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Attendance entity.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Get all attendance records for a specific member
    List<Attendance> findByMemberMemberId(Long memberId);

    // Get all attendance records for a specific date (today's attendance)
    List<Attendance> findByDate(LocalDate date);

    // Get all attendance records for a specific date with pagination
    Page<Attendance> findByDate(LocalDate date, Pageable pageable);

    // Check if member already has an attendance record for today
    // Used to prevent duplicate check-ins on same day
    Optional<Attendance> findByMemberMemberIdAndDate(Long memberId, LocalDate date);

    // Get attendance records for a member within a date range
    List<Attendance> findByMemberMemberIdAndDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);
}