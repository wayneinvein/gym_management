package com.gym.management.system.service.implementationclasses;

import com.gym.management.system.dto.mapper.MemberPaymentDTOMapper;
import com.gym.management.system.dto.mapper.MembershipDTOMapper;
import com.gym.management.system.dto.response.DashboardResponseDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.enums.MemberStatus;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.repository.*;
import com.gym.management.system.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Service implementation for dashboard and reports.
 *
 * Aggregates data from all modules to provide
 * a complete overview of gym operations.
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final AttendanceRepository attendanceRepository;
    private final MembershipRepository membershipRepository;
    private final MemberPaymentRepository memberPaymentRepository;
    private final TrainerPaymentRepository trainerPaymentRepository;
    private final MemberPaymentDTOMapper memberPaymentDTOMapper;
    private final MembershipDTOMapper membershipDTOMapper;

    /**
     * Aggregates all gym stats into a single dashboard response.
     * Covers members, trainers, attendance, memberships, and financials.
     */
    @Override
    public DashboardResponseDTO getStats() {

        DashboardResponseDTO stats = new DashboardResponseDTO();

        // --- Member Stats ---

        stats.setTotalMembers(memberRepository.count());

        stats.setActiveMembers(
                memberRepository.countByStatus(MemberStatus.ACTIVE));

        stats.setInactiveMembers(
                memberRepository.countByStatus(MemberStatus.INACTIVE));

        stats.setSuspendedMembers(
                memberRepository.countByStatus(MemberStatus.SUSPENDED));

        // New members registered this month
        LocalDate firstDayOfMonth = YearMonth.now().atDay(1);
        LocalDate lastDayOfMonth = YearMonth.now().atEndOfMonth();
        stats.setNewMembersThisMonth(
                memberRepository.countByJoinedDateBetween(firstDayOfMonth, lastDayOfMonth));

        // --- Trainer Stats ---

        stats.setTotalTrainers(trainerRepository.count());

        stats.setActiveTrainers(trainerRepository.countByActiveTrue());

        // --- Attendance Stats ---

        // Count today's check-ins
        stats.setTodayAttendanceCount(
                attendanceRepository.countByDate(LocalDate.now()));

        // --- Membership Stats ---

        stats.setActiveMemberships(
                membershipRepository.countByStatus(MembershipStatus.ACTIVE));

        // Memberships expiring in next 7 days
        stats.setMembershipsExpiringInWeek(
                membershipRepository.countByEndDateBeforeAndStatus(
                        LocalDate.now().plusDays(7), MembershipStatus.ACTIVE));

        // --- Financial Stats ---

        // Revenue this month — sum of all PAID member payments this month
        Double revenueThisMonth = memberPaymentRepository
                .sumAmountByStatusAndPaymentDateBetween(
                        PaymentStatus.PAID, firstDayOfMonth, lastDayOfMonth);
        stats.setRevenueThisMonth(revenueThisMonth != null ? revenueThisMonth : 0.0);

        // Salary expenses this month — sum of all PAID trainer payments this month
        Double salaryThisMonth = trainerPaymentRepository
                .sumAmountByStatusAndPaymentDateBetween(
                        PaymentStatus.PAID, firstDayOfMonth, lastDayOfMonth);
        stats.setSalaryExpensesThisMonth(salaryThisMonth != null ? salaryThisMonth : 0.0);

        // Net profit = revenue - salary expenses
        stats.setNetProfitThisMonth(
                stats.getRevenueThisMonth() - stats.getSalaryExpensesThisMonth());

        // Total revenue of all time
        Double totalRevenue = memberPaymentRepository.sumAllPaidAmounts();
        stats.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);

        // Overdue payments count
        stats.setOverduePaymentsCount(
                memberPaymentRepository.countByStatus(PaymentStatus.OVERDUE));

        return stats;
    }

    /**
     * Returns memberships expiring within the next N days.
     */
    @Override
    public List<MembershipResponseDTO> getExpiringMemberships(int days) {
        LocalDate expiryThreshold = LocalDate.now().plusDays(days);
        return membershipRepository
                .findByEndDateBeforeAndStatus(expiryThreshold, MembershipStatus.ACTIVE)
                .stream()
                .map(membershipDTOMapper::toResponse)
                .toList();
    }

    /**
     * Returns all overdue member payments.
     */
    @Override
    public List<MemberPaymentResponseDTO> getOverduePayments() {
        return memberPaymentDTOMapper.toResponse(
                memberPaymentRepository.findByStatus(PaymentStatus.OVERDUE));
    }
}