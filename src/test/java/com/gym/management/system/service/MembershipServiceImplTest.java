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
}
