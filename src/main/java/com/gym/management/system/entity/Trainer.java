package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing a trainer in the gym.
 * Maintains trainer details and relationship with members.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"members"}) // avoids recursive loops in logs
@JsonPropertyOrder({"trainerId", "trainerName", "trainerGender", "phoneNumber"}) // controls JSON response order
public class Trainer {

    // Primary key (auto-generated)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    // Name of the trainer
    private String trainerName;

    // Gender of the trainer
    private String trainerGender;

    // Contact number (limited length)
    @Column(length = 15)
    private String phoneNumber;

    // One trainer can have multiple members
    @OneToMany(mappedBy = "trainer")
    @JsonIgnore // prevents infinite recursion during serialization
    private List<Member> members;
}