package com.gym.management.system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity representing a member's attendance record.
 *
 * A new record is created each time a member checks in.
 * Check-out time is updated when member leaves.
 * One record per member per day — a member cannot check in twice on the same day.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member"})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    // Member this attendance record belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Date of attendance
    @Column(nullable = false)
    private LocalDate date;

    // Time member checked in
    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime;

    // Time member checked out — null if member hasn't checked out yet
    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    // Automatically set when attendance record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}