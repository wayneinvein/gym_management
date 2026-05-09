package com.gym.management.system.service;

import com.gym.management.system.dto.mapper.MembershipDTOMapper;
import com.gym.management.system.dto.request.MembershipRequestDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import com.gym.management.system.dto.response.MembershipSummaryResponseDTO;
import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.enums.MembershipStatus;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.exception.InvalidInputException;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.repository.MemberPaymentRepository;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.repository.MembershipRepository;
import com.gym.management.system.service.implementationclasses.MembershipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceImplTest {

    @Mock
    private MembershipRepository membershipRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private MembershipPlanRepository membershipPlanRepository;
    @Mock private MembershipDTOMapper membershipDTOMapper;
    @Mock private MemberPaymentRepository memberPaymentRepository;

    @InjectMocks
    private MembershipServiceImpl membershipService;

    // ─── Shared test data, reused across all tests ───────────────────────

    private Member member;
    private MembershipPlan activePlan;
    private Membership activeMembership;
    private MembershipRequestDTO requestDTO;
    private MembershipResponseDTO responseDTO;

    @BeforeEach
    void setUp() {

        // Fake member
        member = new Member();
        member.setMemberId(1L);

        // Fake active plan
        activePlan = new MembershipPlan();
        activePlan.setName("Gold Plan");
        activePlan.setDescription("Premium access");
        activePlan.setDurationDays(30);
        activePlan.setPrice(999.0);
        activePlan.setActive(true);

        // Fake active membership
        activeMembership = new Membership();
        activeMembership.setMembershipId(100L);
        activeMembership.setMember(member);
        activeMembership.setPlan(activePlan);
        activeMembership.setStartDate(LocalDate.now());
        activeMembership.setEndDate(LocalDate.now().plusDays(30));
        activeMembership.setStatus(MembershipStatus.ACTIVE);

        // Fake request DTO (what admin sends when creating a membership)
        requestDTO = new MembershipRequestDTO();
        requestDTO.setStartDate(LocalDate.of(2025, 1, 1));

        // Fake response DTO (what service returns after mapping)
        responseDTO = new MembershipResponseDTO();
    }


    // ════════════════════════════════════════════════════════════
    // createMembership() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void createMembership_ShouldReturnResponse_WhenMemberAndPlanExist() {

        // Arrange — tell fakes what to return
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(membershipPlanRepository.findById(10L)).thenReturn(Optional.of(activePlan));
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.empty()); // no existing active membership
        when(membershipRepository.save(any(Membership.class))).thenReturn(activeMembership);
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act — call the real method
        MembershipResponseDTO result = membershipService.createMembership(1L, 10L, requestDTO);

        // Assert — result should not be null
        assertNotNull(result);

        // Verify payment was auto-created with PENDING status
        verify(memberPaymentRepository).save(any(MemberPayment.class));
    }

    @Test
    void createMembership_ShouldCancelExistingMembership_WhenOneAlreadyExists() {

        // Arrange — member already has an active membership
        Membership existingMembership = new Membership();
        existingMembership.setStatus(MembershipStatus.ACTIVE);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(membershipPlanRepository.findById(10L)).thenReturn(Optional.of(activePlan));
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.of(existingMembership)); // existing one found
        when(membershipRepository.save(any(Membership.class))).thenReturn(activeMembership);
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act
        membershipService.createMembership(1L, 10L, requestDTO);

        // Assert — old membership status should be CANCELLED
        assertEquals(MembershipStatus.CANCELLED, existingMembership.getStatus());

        // save() called twice: once to cancel old, once to save new
        verify(membershipRepository, times(2)).save(any(Membership.class));
    }

    @Test
    void createMembership_ShouldThrowNotFoundException_WhenMemberDoesNotExist() {

        // Arrange — member not found in DB
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert — should throw NotFoundException
        assertThrows(NotFoundException.class,
                () -> membershipService.createMembership(99L, 10L, requestDTO));
    }

    @Test
    void createMembership_ShouldThrowNotFoundException_WhenPlanDoesNotExist() {

        // Arrange — member exists but plan does not
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(membershipPlanRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert — should throw NotFoundException
        assertThrows(NotFoundException.class,
                () -> membershipService.createMembership(1L, 99L, requestDTO));
    }

    @Test
    void createMembership_ShouldThrowInvalidInputException_WhenPlanIsInactive() {

        // Arrange — plan exists but is inactive
        activePlan.setActive(false);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(membershipPlanRepository.findById(10L)).thenReturn(Optional.of(activePlan));

        // Act & Assert — should throw InvalidInputException
        assertThrows(InvalidInputException.class,
                () -> membershipService.createMembership(1L, 10L, requestDTO));
    }

    // ════════════════════════════════════════════════════════════
    // getMembershipsByMemberId() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getMembershipsByMemberId_ShouldReturnList_WhenMemberExists() {

        // Arrange
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(membershipRepository.findByMemberMemberId(1L)).thenReturn(List.of(activeMembership));
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act
        List<MembershipResponseDTO> result = membershipService.getMembershipsByMemberId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getMembershipsByMemberId_ShouldReturnEmptyList_WhenMemberHasNoMemberships() {

        // Arrange — member exists but has no memberships
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(membershipRepository.findByMemberMemberId(1L)).thenReturn(List.of());

        // Act
        List<MembershipResponseDTO> result = membershipService.getMembershipsByMemberId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getMembershipsByMemberId_ShouldThrowNotFoundException_WhenMemberDoesNotExist() {

        // Arrange — member not found
        when(memberRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> membershipService.getMembershipsByMemberId(99L));
    }

    // ════════════════════════════════════════════════════════════
    // getActiveMembership() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getActiveMembership_ShouldReturnResponse_WhenActiveMembershipExists() {

        // Arrange
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.of(activeMembership));
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act
        MembershipResponseDTO result = membershipService.getActiveMembership(1L);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getActiveMembership_ShouldThrowNotFoundException_WhenMemberDoesNotExist() {

        // Arrange
        when(memberRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> membershipService.getActiveMembership(99L));
    }

    @Test
    void getActiveMembership_ShouldThrowNotFoundException_WhenNoActiveMembershipFound() {

        // Arrange — member exists but has no active membership
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> membershipService.getActiveMembership(1L));
    }

    // ════════════════════════════════════════════════════════════
    // getAllMemberships() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getAllMemberships_ShouldReturnPagedResult_WhenCalled() {

        // Arrange — wrap membership in a Page object (what repository returns)
        Page<Membership> membershipPage = new PageImpl<>(List.of(activeMembership));
        when(membershipRepository.findAll(any(PageRequest.class))).thenReturn(membershipPage);
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act
        Page<MembershipResponseDTO> result = membershipService.getAllMemberships(0, 10, "startDate", "asc");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllMemberships_ShouldReturnEmptyPage_WhenNoMembershipsExist() {

        // Arrange — empty page
        when(membershipRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        // Act
        Page<MembershipResponseDTO> result = membershipService.getAllMemberships(0, 10, "startDate", "asc");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    // ════════════════════════════════════════════════════════════
    // cancelMembership() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void cancelMembership_ShouldReturnResponse_WhenMembershipIsActive() {

        // Arrange
        when(membershipRepository.findById(100L)).thenReturn(Optional.of(activeMembership));
        when(membershipRepository.save(activeMembership)).thenReturn(activeMembership);
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act
        MembershipResponseDTO result = membershipService.cancelMembership(100L);

        // Assert — result not null and status updated to CANCELLED
        assertNotNull(result);
        assertEquals(MembershipStatus.CANCELLED, activeMembership.getStatus());
    }

    @Test
    void cancelMembership_ShouldThrowNotFoundException_WhenMembershipDoesNotExist() {

        // Arrange
        when(membershipRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> membershipService.cancelMembership(999L));
    }

    @Test
    void cancelMembership_ShouldThrowInvalidInputException_WhenMembershipIsAlreadyCancelled() {

        // Arrange — membership already cancelled
        activeMembership.setStatus(MembershipStatus.CANCELLED);
        when(membershipRepository.findById(100L)).thenReturn(Optional.of(activeMembership));

        // Act & Assert
        assertThrows(InvalidInputException.class,
                () -> membershipService.cancelMembership(100L));
    }

    @Test
    void cancelMembership_ShouldThrowInvalidInputException_WhenMembershipIsExpired() {

        // Arrange — expired memberships also cannot be cancelled
        activeMembership.setStatus(MembershipStatus.EXPIRED);
        when(membershipRepository.findById(100L)).thenReturn(Optional.of(activeMembership));

        // Act & Assert
        assertThrows(InvalidInputException.class,
                () -> membershipService.cancelMembership(100L));
    }

    // ════════════════════════════════════════════════════════════
    // getExpiringMemberships() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getExpiringMemberships_ShouldReturnList_WhenMembershipsExpiringSoon() {

        // Arrange
        when(membershipRepository.findByEndDateBeforeAndStatus(any(LocalDate.class), eq(MembershipStatus.ACTIVE)))
                .thenReturn(List.of(activeMembership));
        when(membershipDTOMapper.toResponse(activeMembership)).thenReturn(responseDTO);

        // Act
        List<MembershipResponseDTO> result = membershipService.getExpiringMemberships(7);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getExpiringMemberships_ShouldReturnEmptyList_WhenNoMembershipsExpiringSoon() {

        // Arrange — nothing expiring in next 3 days
        when(membershipRepository.findByEndDateBeforeAndStatus(any(LocalDate.class), eq(MembershipStatus.ACTIVE)))
                .thenReturn(List.of());

        // Act
        List<MembershipResponseDTO> result = membershipService.getExpiringMemberships(3);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // ════════════════════════════════════════════════════════════
    // getMembershipSummary() tests
    // ════════════════════════════════════════════════════════════

    @Test
    void getMembershipSummary_ShouldReturnSummary_WhenActiveMembershipExists() {

        // Arrange — membership started 10 days ago, ends in 20 days
        activeMembership.setStartDate(LocalDate.now().minusDays(10));
        activeMembership.setEndDate(LocalDate.now().plusDays(20));

        when(memberRepository.findByUserUsername("john_doe")).thenReturn(Optional.of(member));
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.of(activeMembership));
        when(memberPaymentRepository.findByMembershipMembershipIdAndStatus(100L, PaymentStatus.PENDING))
                .thenReturn(Optional.empty()); // no pending payment

        // Act
        MembershipSummaryResponseDTO result = membershipService.getMembershipSummary("john_doe");

        // Assert — basic fields should be populated correctly
        assertNotNull(result);
        assertEquals("Gold Plan", result.getPlanName());
        assertEquals(30L, result.getTotalDays());
        assertEquals(10L, result.getDaysCompleted());
        assertEquals(20L, result.getDaysRemaining());
        assertFalse(result.isPaymentPending());
        assertEquals(0.0, result.getAmountDue());
    }

    @Test
    void getMembershipSummary_ShouldShowPendingPayment_WhenPaymentNotYetPaid() {

        // Arrange — pending payment exists for this membership
        MemberPayment pendingPayment = new MemberPayment();
        pendingPayment.setAmount(999.0);
        pendingPayment.setStatus(PaymentStatus.PENDING);

        activeMembership.setStartDate(LocalDate.now().minusDays(10));
        activeMembership.setEndDate(LocalDate.now().plusDays(20));

        when(memberRepository.findByUserUsername("john_doe")).thenReturn(Optional.of(member));
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.of(activeMembership));
        when(memberPaymentRepository.findByMembershipMembershipIdAndStatus(100L, PaymentStatus.PENDING))
                .thenReturn(Optional.of(pendingPayment));

        // Act
        MembershipSummaryResponseDTO result = membershipService.getMembershipSummary("john_doe");

        // Assert — payment flag and amount should reflect pending payment
        assertNotNull(result);
        assertTrue(result.isPaymentPending());
        assertEquals(999.0, result.getAmountDue());
    }

    @Test
    void getMembershipSummary_ShouldThrowNotFoundException_WhenMemberNotFound() {

        // Arrange — no member linked to this username
        when(memberRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> membershipService.getMembershipSummary("unknown"));
    }

    @Test
    void getMembershipSummary_ShouldThrowNotFoundException_WhenNoActiveMembershipFound() {

        // Arrange — member exists but has no active membership
        when(memberRepository.findByUserUsername("john_doe")).thenReturn(Optional.of(member));
        when(membershipRepository.findByMemberMemberIdAndStatus(1L, MembershipStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> membershipService.getMembershipSummary("john_doe"));
    }
}

}
