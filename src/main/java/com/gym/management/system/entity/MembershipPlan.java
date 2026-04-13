package com.gym.management.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a membership plan.
 * Defines duration, pricing, and availability of plans.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlan {

    // Primary key (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    // Unique name of the plan (e.g., Monthly, Yearly)
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // Duration of the plan in days
    @Column(nullable = false)
    private int durationDays;

    // Price of the plan
    @Column(nullable = false)
    private double price;

    // Indicates whether the plan is active and available for use
    @Column(nullable = false)
    private boolean active = true;
}