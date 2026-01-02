package com.gym.management.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private int durationDays;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean active = true;
}
