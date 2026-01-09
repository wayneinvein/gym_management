package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gym.management.system.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"member", "plan"})
@JsonPropertyOrder({"membershipId", "startDate", "endDate", "price", "status", "plan"})
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status; // ACTIVE, EXPIRED, UPCOMING

    @OneToOne
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    @JsonIgnore
    private Members member;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnore
    private MembershipPlan plan;
}
