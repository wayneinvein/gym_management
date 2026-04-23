package com.gym.management.system.dto.response;

import lombok.Data;

/**
 * DTO for returning overall gym stats in the dashboard.
 *
 * Aggregates data from members, trainers, attendance,
 * memberships, and payments into a single response.
 */
@Data
public class DashboardResponseDTO {

    // --- Member Stats ---

    // Total number of members registered in the gym
    private long totalMembers;

    // Number of currently active members
    private long activeMembers;

    // Number of inactive members
    private long inactiveMembers;

    // Number of suspended members
    private long suspendedMembers;

    // New members registered this month
    private long newMembersThisMonth;

    // --- Trainer Stats ---

    // Total number of trainers
    private long totalTrainers;

    // Number of currently active trainers
    private long activeTrainers;

    // --- Attendance Stats ---

    // Number of members who checked in today
    private long todayAttendanceCount;

    // --- Membership Stats ---

    // Number of currently active memberships
    private long activeMemberships;

    // Number of memberships expiring in next 7 days
    private long membershipsExpiringInWeek;

    // --- Financial Stats ---

    // Total member payments received this month
    private double revenueThisMonth;

    // Total trainer salaries paid this month
    private double salaryExpensesThisMonth;

    // Net profit this month (revenue - expenses)
    private double netProfitThisMonth;

    // Total revenue of all time
    private double totalRevenue;

    // Number of overdue member payments
    private long overduePaymentsCount;
}