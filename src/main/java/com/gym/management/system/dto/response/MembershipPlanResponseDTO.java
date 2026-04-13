package com.gym.management.system.dto.response;

import lombok.Data;

@Data
public class MembershipPlanResponseDTO {

    private Long planId;
    private String name;
    private int durationDays;
    private double price;
    private boolean active;
}