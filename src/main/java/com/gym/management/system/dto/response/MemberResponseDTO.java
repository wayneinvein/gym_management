package com.gym.management.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning member details in API responses.
 * Exposes safe and relevant member information to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {

    // Unique identifier of the member
    private Long memberId;

    // Name of the member
    private String memberName;

    // Gender of the member
    private String memberGender;

    // Contact number of the member
    private String phoneNumber;
}