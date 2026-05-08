package com.gym.management.system.service;

import com.gym.management.system.dto.mapper.MemberPaymentDTOMapper;
import com.gym.management.system.dto.request.MemberPaymentRequestDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.entity.Member;
import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.entity.Membership;
import com.gym.management.system.enums.PaymentMethod;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.exception.NotFoundException;
import com.gym.management.system.exception.InvalidInputException;
import com.gym.management.system.repository.MemberPaymentRepository;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipRepository;
import com.gym.management.system.service.implementationclasses.MemberPaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberPaymentServiceImplTest {

    @Mock
    private MemberPaymentRepository memberPaymentRepository;

    @Mock
    private MemberPaymentDTOMapper memberPaymentDTOMapper;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private MemberPaymentServiceImpl memberPaymentService;

    @Test
    void getPendingDues_ShouldReturnList_WhenPendingPaymentsExist() {

        // Arrange — set up fake data
        MemberPayment payment1 = new MemberPayment();
        MemberPayment payment2 = new MemberPayment();

        MemberPaymentResponseDTO dto1 = new MemberPaymentResponseDTO();
        MemberPaymentResponseDTO dto2 = new MemberPaymentResponseDTO();

        // tell fake repository what to return when called
        when(memberPaymentRepository.findByStatusIn(List.of(PaymentStatus.PENDING, PaymentStatus.OVERDUE)))
                .thenReturn(List.of(payment1, payment2));

        // tell fake mapper what to return for each payment
        when(memberPaymentDTOMapper.toResponse(payment1)).thenReturn(dto1);
        when(memberPaymentDTOMapper.toResponse(payment2)).thenReturn(dto2);

        // Act — call the actual method
        List<MemberPaymentResponseDTO> result = memberPaymentService.getPendingDues();

        // Assert — verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPendingDues_ShouldReturnEmptyList_WhenNoPendingPaymentsExist() {

        // Arrange — repository returns empty list
        when(memberPaymentRepository.findByStatusIn(List.of(PaymentStatus.PENDING, PaymentStatus.OVERDUE)))
                .thenReturn(List.of());

        // Act
        List<MemberPaymentResponseDTO> result = memberPaymentService.getPendingDues();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getPendingDues_ShouldCallRepository_WithPendingAndOverdueStatus() {

        // Arrange
        when(memberPaymentRepository.findByStatusIn(List.of(PaymentStatus.PENDING, PaymentStatus.OVERDUE)))
                .thenReturn(List.of());

        // Act
        memberPaymentService.getPendingDues();

        // Assert — verify repository was called exactly once with correct statuses
        verify(memberPaymentRepository, times(1))
                .findByStatusIn(List.of(PaymentStatus.PENDING, PaymentStatus.OVERDUE));
    }

    @Test
    void getPaymentById_ShouldReturnDTO_WhenPaymentExists() {

        // Arrange
        MemberPayment payment = new MemberPayment();
        MemberPaymentResponseDTO dto = new MemberPaymentResponseDTO();

        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(memberPaymentDTOMapper.toResponse(payment)).thenReturn(dto);

        // Act
        MemberPaymentResponseDTO result = memberPaymentService.getPaymentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getPaymentById_ShouldThrowNotFoundException_WhenPaymentDoesNotExist() {

        // Arrange — repository returns empty optional
        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert — verify exception is thrown
        assertThrows(NotFoundException.class, () -> memberPaymentService.getPaymentById(1L));
    }

    @Test
    void recordPayment_ShouldThrowNotFoundException_WhenMemberNotFound() {

        // Arrange
        MemberPaymentRequestDTO dto = new MemberPaymentRequestDTO();
        dto.setMemberId(1L);

        // member does not exist
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> memberPaymentService.recordPayment(dto));
    }

    @Test
    void recordPayment_ShouldThrowNotFoundException_WhenMembershipNotFound() {

        // Arrange
        MemberPaymentRequestDTO dto = new MemberPaymentRequestDTO();
        dto.setMemberId(1L);
        dto.setMembershipId(1L);

        // member exists but membership does not
        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member()));
        when(membershipRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> memberPaymentService.recordPayment(dto));
    }

    @Test
    void recordPayment_ShouldReturnDTO_WhenPaymentRecordedSuccessfully() {

        // Arrange
        MemberPaymentRequestDTO dto = new MemberPaymentRequestDTO();
        dto.setMemberId(1L);
        dto.setMembershipId(1L);
        dto.setAmount(1000.0);
        dto.setPaymentDate(LocalDate.now());
        dto.setPaymentMethod(PaymentMethod.CASH);

        Member member = new Member();
        Membership membership = new Membership();
        MemberPayment savedPayment = new MemberPayment();
        MemberPaymentResponseDTO responseDTO = new MemberPaymentResponseDTO();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));
        when(memberPaymentRepository.save(any(MemberPayment.class))).thenReturn(savedPayment);
        when(memberPaymentDTOMapper.toResponse(savedPayment)).thenReturn(responseDTO);

        // Act
        MemberPaymentResponseDTO result = memberPaymentService.recordPayment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(memberPaymentRepository, times(1)).save(any(MemberPayment.class));
    }

    @Test
    void updatePaymentStatus_ShouldThrowNotFoundException_WhenPaymentNotFound() {

        // Arrange
        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> memberPaymentService.updatePaymentStatus(1L, PaymentStatus.OVERDUE));
    }

    @Test
    void updatePaymentStatus_ShouldReturnDTO_WhenStatusUpdatedSuccessfully() {

        // Arrange
        MemberPayment payment = new MemberPayment();
        MemberPaymentResponseDTO responseDTO = new MemberPaymentResponseDTO();

        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(memberPaymentRepository.save(payment)).thenReturn(payment);
        when(memberPaymentDTOMapper.toResponse(payment)).thenReturn(responseDTO);

        // Act
        MemberPaymentResponseDTO result = memberPaymentService.updatePaymentStatus(1L, PaymentStatus.OVERDUE);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(memberPaymentRepository, times(1)).save(payment);
    }
    @Test
    void markAsPaid_ShouldThrowNotFoundException_WhenPaymentNotFound() {

        // Arrange
        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(NotFoundException.class,
                () -> memberPaymentService.markAsPaid(1L, PaymentMethod.CASH, LocalDate.now(), null));
    }

    @Test
    void markAsPaid_ShouldThrowInvalidInputException_WhenPaymentAlreadyPaid() {

        // Arrange — payment already has PAID status
        MemberPayment payment = new MemberPayment();
        payment.setStatus(PaymentStatus.PAID);

        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // Act + Assert
        assertThrows(InvalidInputException.class,
                () -> memberPaymentService.markAsPaid(1L, PaymentMethod.CASH, LocalDate.now(), null));
    }

    @Test
    void markAsPaid_ShouldReturnDTO_WhenPaymentMarkedSuccessfully() {

        // Arrange — payment is PENDING
        MemberPayment payment = new MemberPayment();
        payment.setStatus(PaymentStatus.PENDING);
        MemberPaymentResponseDTO responseDTO = new MemberPaymentResponseDTO();

        when(memberPaymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(memberPaymentRepository.save(payment)).thenReturn(payment);
        when(memberPaymentDTOMapper.toResponse(payment)).thenReturn(responseDTO);

        // Act
        MemberPaymentResponseDTO result = memberPaymentService.markAsPaid(
                1L, PaymentMethod.CASH, LocalDate.now(), "Paid at counter");

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        assertEquals(PaymentStatus.PAID, payment.getStatus());
        assertEquals(PaymentMethod.CASH, payment.getPaymentMethod());
        verify(memberPaymentRepository, times(1)).save(payment);
    }
}