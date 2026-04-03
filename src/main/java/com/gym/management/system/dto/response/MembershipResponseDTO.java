package com.gym.management.system.dto.response;

import com.gym.management.system.entity.Members;
import com.gym.management.system.entity.MembershipPlan;
import com.gym.management.system.enums.MembershipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponseDTO {
    private Long membershipId;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipStatus status;
    private Members member;
    private MembershipPlan plan;

}
