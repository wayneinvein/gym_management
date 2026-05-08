package com.gym.management.system.service;

import com.gym.management.system.dto.mapper.MemberPaymentDTOMapper;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.entity.MemberPayment;
import com.gym.management.system.enums.PaymentStatus;
import com.gym.management.system.repository.MemberPaymentRepository;
import com.gym.management.system.service.implementationclasses.MemberPaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberPaymentServiceImplTest {

    @Mock
    private MemberPaymentRepository memberPaymentRepository;

    @Mock
    private MemberPaymentDTOMapper memberPaymentDTOMapper;

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
}