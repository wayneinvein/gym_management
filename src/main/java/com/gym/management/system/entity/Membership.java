package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.management.system.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a membership subscription.
 *
 * A membership is created when a member subscribes to a plan.
 * A member can have multiple memberships over time (history).
 * ManyToOne with Member allows full membership history per member.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "plan"})
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    // Start date of this membership — set when subscription is created
    @Column(nullable = false)
    private LocalDate startDate;

    // End date — calculated as startDate + plan.durationDays
    @Column(nullable = false)
    private LocalDate endDate;

    // Current status of this membership
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;

    // ManyToOne so a member can have multiple memberships over time
    // This enables full membership history per member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore // prevent circular reference
    private Member member;

    // The plan this membership is based on
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan plan;

    // Automatically set when membership record is created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}