package com.gym.management.system.service.interfaces;

import com.gym.management.system.dto.response.DashboardResponseDTO;
import com.gym.management.system.dto.response.MemberPaymentResponseDTO;
import com.gym.management.system.dto.response.MembershipResponseDTO;
import java.util.List;

/**
 * Service interface for dashboard and reports.
 */
public interface DashboardService {

    // Get overall gym stats
    DashboardResponseDTO getStats();

    // Get memberships expiring in next N days
    List<MembershipResponseDTO> getExpiringMemberships(int days);

    // Get all overdue member payments
    List<MemberPaymentResponseDTO> getOverduePayments();
}