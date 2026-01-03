package com.gym.management.system.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String memberName;
    private String memberGender;
    private String memberPhoneNumber;
}
