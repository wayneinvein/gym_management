package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a gym member.
 * Maps member details and relationships with trainer, membership, and user.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"trainer", "membership"}) // avoid recursive loops in logs
@JsonPropertyOrder({"memberId", "memberName", "memberGender", "phoneNumber", "membership", "trainer"})
public class Members {

    // Primary key (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    // Name of the member
    @Column(name = "member_name")
    private String memberName;

    // Gender of the member
    @Column(name = "member_gender")
    private String memberGender;

    // Contact number of the member
    @Column(name = "phone_number")
    private String phoneNumber;

    // Many members can be assigned to one trainer
    @ManyToOne
    @JsonIgnore // prevents infinite recursion in JSON response
    @JoinColumn(name = "trainer_id")
    private Trainers trainer;

    // One-to-one relationship with membership (mapped by Membership entity)
    @JsonIgnore // prevents circular reference
    @OneToOne(mappedBy = "member")
    private Membership membership;

    // Many members can be created/managed by one user (admin/receptionist)
    @ManyToOne
    private User user;
}