package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"memberId", "memberName", "memberGender", "phoneNumber", "membership", "trainer"}) //to set the order of response
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String memberName;
    private String memberGender;
    @Column(length = 15)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "trainer_id")   // foreign key in members table
    private Trainers trainer;

    @OneToOne(mappedBy = "member")
    private Membership membership;
}
