package com.gym.management.system.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRequestDTO {

    private String trainerName;
    private String trainerGender;
    private String phoneNumber;
}