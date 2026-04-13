package com.gym.management.system.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponseDTO {

    private Long trainerId;
    private String trainerName;
    private String trainerGender;
    private String phoneNumber;
}