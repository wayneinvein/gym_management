package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"trainer", "membership"})
@JsonPropertyOrder({"memberId", "memberName", "memberGender", "phoneNumber", "membership", "trainer"})
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_gender")
    private String memberGender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "trainer_id")
    private Trainers trainer;

    @JsonIgnore
    @OneToOne(mappedBy = "member")
    private Membership membership;

    @ManyToOne
    private User user;
}
