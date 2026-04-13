package com.gym.management.system.dto.request;

import lombok.Data;

@Data
public class MembershipPlanRequestDTO {

    private String name;
    private int durationDays;
    private double price;
    private boolean active; // optional (can default to true if not sent)
}