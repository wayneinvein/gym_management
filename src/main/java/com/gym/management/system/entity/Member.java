package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.management.system.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a gym member.
 *
 * A member is a person who has joined the gym.
 * Each member has a login account (User), can be assigned a trainer,
 * and can have multiple memberships over time (membership history).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"trainer", "memberships", "user"}) // exclude relationships to avoid recursive toString calls
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    // Full name of the member
    @Column(name = "member_name", nullable = false)
    private String memberName;

    // Gender of the member (e.g., Male, Female, Other)
    @Column(name = "member_gender", nullable = false)
    private String memberGender;

    // Unique phone number used as login username when member account is created
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    // Optional email address for communication
    @Column(name = "email", unique = true)
    private String email;

    // Physical address of the member
    @Column(name = "address")
    private String address;

    // Date of birth — used for age calculation and birthday tracking
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    // Current status of the member in the gym
    // Defaults to ACTIVE when a member is first created
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    // Date when the member first joined the gym
    // Set automatically at creation time, never changes
    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    // Automatically set when the record is first created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically updated every time the record is modified
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Trainer assigned to this member — nullable (member may not have a trainer)
    // Many members can be assigned to one trainer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    @JsonIgnore // prevent trainer → members → trainer infinite loop during JSON serialization
    private Trainer trainer;

    // Full membership history of this member
    // OneToMany because a member can renew/change plans multiple times over time
    // cascade ALL means if member is deleted, all their memberships are deleted too
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore // prevent circular reference during serialization
    private List<Membership> memberships;

    // Login account linked to this member
    // OneToOne because each member has exactly one login account
    // nullable — in case member is added without a login account (walk-in, manual entry)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore // User contains sensitive fields like password — never expose in response
    private User user;
}