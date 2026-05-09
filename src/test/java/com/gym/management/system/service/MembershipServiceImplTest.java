package com.gym.management.system.service;

import com.gym.management.system.dto.mapper.MembershipDTOMapper;
import com.gym.management.system.repository.MemberPaymentRepository;
import com.gym.management.system.repository.MemberRepository;
import com.gym.management.system.repository.MembershipPlanRepository;
import com.gym.management.system.repository.MembershipRepository;
import com.gym.management.system.service.implementationclasses.MembershipServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
