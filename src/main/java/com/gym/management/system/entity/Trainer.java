package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a gym trainer.
 *
 * A trainer is a staff member who trains gym members.
 * Each trainer can have a login account (User) to access the system,
 * view their assigned members, and manage workout/diet plans.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"members", "user"}) // exclude relationships to avoid recursive toString
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    // Full name of the trainer
    @Column(nullable = false)
    private String trainerName;

    // Gender of the trainer (e.g., Male, Female, Other)
    @Column(nullable = false)
    private String trainerGender;

    // Unique phone number — also used as login username
    @Column(length = 15, unique = true, nullable = false)
    private String phoneNumber;

    // Optional email address for communication
    @Column(unique = true)
    private String email;

    // Area of expertise (e.g., Weight Training, Yoga, Cardio)
    @Column(length = 100)
    private String specialization;

    // Date when trainer joined the gym
    @Column(name = "joining_date")
    private LocalDate joiningDate;

    // Monthly salary of the trainer — used for trainer payment tracking
    @Column(nullable = false)
    private double salary;

    // Whether the trainer is currently active or not
    // Inactive trainers cannot log in or be assigned new members
    @Column(nullable = false)
    private boolean active = true;

    // Login account linked to this trainer
    // OneToOne because each trainer has exactly one login account
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore // User contains sensitive fields like password — never expose in response
    private User user;

    // List of members assigned to this trainer
    // mappedBy means the foreign key is on the Members side
    @OneToMany(mappedBy = "trainer")
    @JsonIgnore // prevent infinite recursion during serialization
    private List<Member> members;

    // Automatically set when the record is first created
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically updated every time the record is modified
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}