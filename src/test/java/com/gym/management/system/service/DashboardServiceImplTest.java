package com.gym.management.system.service;

import com.gym.management.system.service.implementationclasses.DashboardServiceImpl;
import com.gym.management.system.dto.mapper.MemberPaymentDTOMapper;
import com.gym.management.system.dto.mapper.MembershipDTOMapper;
import com.gym.management.system.dto.response.DashboardResponseDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.MemberStatus;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MemberPaymentRepository memberPaymentRepository;

    @Mock
    private TrainerPaymentRepository trainerPaymentRepository;

    @Mock
    private MemberPaymentDTOMapper memberPaymentDTOMapper;

    @Mock
    private MembershipDTOMapper membershipDTOMapper;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    // ─── Shared date range used in multiple tests ────────────────────────

    private LocalDate firstDayOfMonth;
    private LocalDate lastDayOfMonth;

    @BeforeEach
    void setUp() {
        firstDayOfMonth = YearMonth.now().atDay(1);
        lastDayOfMonth = YearMonth.now().atEndOfMonth();
    }

    // ════════════════════════════════════════════════════════════
    // getStats() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getStats_ShouldReturnDashboardResponse_WhenCalled() {

        // Arrange — stub all repository calls with fake counts and amounts
        when(memberRepository.count()).thenReturn(100L);
        when(memberRepository.countByStatus(MemberStatus.ACTIVE)).thenReturn(80L);
        when(memberRepository.countByStatus(MemberStatus.INACTIVE)).thenReturn(10L);
        when(memberRepository.countByStatus(MemberStatus.SUSPENDED)).thenReturn(10L);
        when(memberRepository.countByJoinedDateBetween(firstDayOfMonth, lastDayOfMonth)).thenReturn(5L);

        when(trainerRepository.count()).thenReturn(10L);
        when(trainerRepository.countByActiveTrue()).thenReturn(8L);

        when(attendanceRepository.countByDate(LocalDate.now())).thenReturn(30L);

        when(membershipRepository.countByStatus(MembershipStatus.ACTIVE)).thenReturn(75L);
        when(membershipRepository.countByEndDateBeforeAndStatus(
                LocalDate.now().plusDays(7), MembershipStatus.ACTIVE)).thenReturn(5L);

        when(memberPaymentRepository.sumAmountByStatusAndPaymentDateBetween(
                PaymentStatus.PAID, firstDayOfMonth, lastDayOfMonth)).thenReturn(50000.0);
        when(trainerPaymentRepository.sumAmountByStatusAndPaymentDateBetween(
                PaymentStatus.PAID, firstDayOfMonth, lastDayOfMonth)).thenReturn(20000.0);
        when(memberPaymentRepository.sumAllPaidAmounts()).thenReturn(500000.0);
        when(memberPaymentRepository.countByStatus(PaymentStatus.OVERDUE)).thenReturn(3L);

        // Act
        DashboardResponseDTO result = dashboardService.getStats();

        // Assert — all fields should match what the repositories returned
        assertNotNull(result);
        assertEquals(100L, result.getTotalMembers());
        assertEquals(80L, result.getActiveMembers());
        assertEquals(10L, result.getInactiveMembers());
        assertEquals(10L, result.getSuspendedMembers());
        assertEquals(5L, result.getNewMembersThisMonth());
        assertEquals(10L, result.getTotalTrainers());
        assertEquals(8L, result.getActiveTrainers());
        assertEquals(30L, result.getTodayAttendanceCount());
        assertEquals(75L, result.getActiveMemberships());
        assertEquals(5L, result.getMembershipsExpiringInWeek());
        assertEquals(50000.0, result.getRevenueThisMonth());
        assertEquals(20000.0, result.getSalaryExpensesThisMonth());
        assertEquals(30000.0, result.getNetProfitThisMonth()); // 50000 - 20000
        assertEquals(500000.0, result.getTotalRevenue());
        assertEquals(3L, result.getOverduePaymentsCount());
    }

    @Test
    void getStats_ShouldSetRevenueToZero_WhenNoPaymentsThisMonth() {

        // Arrange — revenue query returns null (no payments yet this month)
        when(memberRepository.count()).thenReturn(0L);
        when(memberRepository.countByStatus(any())).thenReturn(0L);
        when(memberRepository.countByJoinedDateBetween(any(), any())).thenReturn(0L);
        when(trainerRepository.count()).thenReturn(0L);
        when(trainerRepository.countByActiveTrue()).thenReturn(0L);
        when(attendanceRepository.countByDate(any())).thenReturn(0L);
        when(membershipRepository.countByStatus(any())).thenReturn(0L);
        when(membershipRepository.countByEndDateBeforeAndStatus(any(), any())).thenReturn(0L);
        when(memberPaymentRepository.countByStatus(any())).thenReturn(0L);

        // DB returns null when SUM has no rows — service should convert to 0.0
        when(memberPaymentRepository.sumAmountByStatusAndPaymentDateBetween(any(), any(), any()))
                .thenReturn(null);
        when(trainerPaymentRepository.sumAmountByStatusAndPaymentDateBetween(any(), any(), any()))
                .thenReturn(null);
        when(memberPaymentRepository.sumAllPaidAmounts()).thenReturn(null);

        // Act
        DashboardResponseDTO result = dashboardService.getStats();

        // Assert — nulls from DB should be treated as 0.0, not cause NPE
        assertNotNull(result);
        assertEquals(0.0, result.getRevenueThisMonth());
        assertEquals(0.0, result.getSalaryExpensesThisMonth());
        assertEquals(0.0, result.getNetProfitThisMonth());
        assertEquals(0.0, result.getTotalRevenue());
    }

    @Test
    void getStats_ShouldCalculateNetProfitCorrectly_WhenRevenueAndSalaryArePresent() {

        // Arrange — set up just the financial fields we care about
        when(memberRepository.count()).thenReturn(0L);
        when(memberRepository.countByStatus(any())).thenReturn(0L);
        when(memberRepository.countByJoinedDateBetween(any(), any())).thenReturn(0L);
        when(trainerRepository.count()).thenReturn(0L);
        when(trainerRepository.countByActiveTrue()).thenReturn(0L);
        when(attendanceRepository.countByDate(any())).thenReturn(0L);
        when(membershipRepository.countByStatus(any())).thenReturn(0L);
        when(membershipRepository.countByEndDateBeforeAndStatus(any(), any())).thenReturn(0L);
        when(memberPaymentRepository.countByStatus(any())).thenReturn(0L);
        when(memberPaymentRepository.sumAllPaidAmounts()).thenReturn(0.0);

        when(memberPaymentRepository.sumAmountByStatusAndPaymentDateBetween(
                PaymentStatus.PAID, firstDayOfMonth, lastDayOfMonth)).thenReturn(80000.0);
        when(trainerPaymentRepository.sumAmountByStatusAndPaymentDateBetween(
                PaymentStatus.PAID, firstDayOfMonth, lastDayOfMonth)).thenReturn(30000.0);

        // Act
        DashboardResponseDTO result = dashboardService.getStats();

        // Assert — net profit = revenue - salary = 80000 - 30000 = 50000
        assertEquals(50000.0, result.getNetProfitThisMonth());
    }

    // ════════════════════════════════════════════════════════════
    // getExpiringMemberships() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getExpiringMemberships_ShouldReturnList_WhenMembershipsExpiringSoon() {

        // Arrange
        Membership membership = new Membership();
        MembershipResponseDTO responseDTO = new MembershipResponseDTO();

        when(membershipRepository.findByEndDateBeforeAndStatus(
                LocalDate.now().plusDays(7), MembershipStatus.ACTIVE))
                .thenReturn(List.of(membership));
        when(membershipDTOMapper.toResponse(membership)).thenReturn(responseDTO);

        // Act
        List<MembershipResponseDTO> result = dashboardService.getExpiringMemberships(7);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getExpiringMemberships_ShouldReturnEmptyList_WhenNoMembershipsExpiringSoon() {

        // Arrange — nothing expiring in next 7 days
        when(membershipRepository.findByEndDateBeforeAndStatus(any(), eq(MembershipStatus.ACTIVE)))
                .thenReturn(List.of());

        // Act
        List<MembershipResponseDTO> result = dashboardService.getExpiringMemberships(7);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ════════════════════════════════════════════════════════════
    // getOverduePayments() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getOverduePayments_ShouldReturnList_WhenOverduePaymentsExist() {

        // Arrange
        MemberPayment payment = new MemberPayment();
        MemberPaymentResponseDTO responseDTO = new MemberPaymentResponseDTO();

        when(memberPaymentRepository.findByStatus(PaymentStatus.OVERDUE))
                .thenReturn(List.of(payment));
        when(memberPaymentDTOMapper.toResponse(List.of(payment)))
                .thenReturn(List.of(responseDTO));

        // Act
        List<MemberPaymentResponseDTO> result = dashboardService.getOverduePayments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getOverduePayments_ShouldReturnEmptyList_WhenNoOverduePaymentsExist() {

        // Arrange — no overdue payments
        when(memberPaymentRepository.findByStatus(PaymentStatus.OVERDUE))
                .thenReturn(List.of());
        when(memberPaymentDTOMapper.toResponse(List.of()))
                .thenReturn(List.of());

        // Act
        List<MemberPaymentResponseDTO> result = dashboardService.getOverduePayments();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}