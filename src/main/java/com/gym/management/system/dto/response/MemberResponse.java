package com.gym.management.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private boolean active;
}
