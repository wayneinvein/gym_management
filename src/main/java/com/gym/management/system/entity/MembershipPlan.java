package com.gym.management.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a membership plan offered by the gym.
 *
 * Plans define the duration and pricing of a membership.
 * Members are assigned one of these plans when they subscribe.
 * Multiple members can be on the same plan.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    // Unique name of the plan (e.g., Monthly, Quarterly, Yearly)
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // Optional description of what the plan includes
    @Column(length = 255)
    private String description;

    // Duration of the plan in days (e.g., 30 for monthly, 365 for yearly)
    @Column(nullable = false)
    private int durationDays;

    // Price of the plan in rupees
    @Column(nullable = false)
    private double price;

    // Whether this plan is currently active and available for new subscriptions
    // Inactive plans cannot be assigned to new members
    @Column(nullable = false)
    private boolean active = true;
}