package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gym.management.system.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity representing a membership assigned to a member.
 * Stores duration, status, and associated plan details.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "plan"}) // avoids recursive calls in logs
@JsonPropertyOrder({"membershipId", "startDate", "endDate", "price", "status", "plan"})
public class Membership {

    // Primary key (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    // Start date of the membership
    private LocalDate startDate;

    // End date calculated based on plan duration
    private LocalDate endDate;

    // Membership status (ACTIVE, EXPIRED, UPCOMING)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;

    // One-to-one relationship with member (each member has one membership)
    @OneToOne
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    @JsonIgnore // prevents circular reference during JSON serialization
    private Members member;

    // Many memberships can use the same plan
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnore // prevents nested object recursion in response
    private MembershipPlan plan;
}