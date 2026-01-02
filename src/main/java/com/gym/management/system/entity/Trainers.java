package com.gym.management.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"trainerId", "trainerName", "trainerGender", "phoneNumber"}) //to set the order of response
public class Trainers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;
    private String trainerName;
    private String trainerGender;
    @Column(length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "trainer")
    @JsonIgnore
    private List<Members> members;
}
