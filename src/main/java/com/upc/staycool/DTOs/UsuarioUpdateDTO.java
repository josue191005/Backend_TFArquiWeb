package com.upc.staycool.DTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioUpdateDTO {
    private String name;
    private String email;
    private Integer age;
    private String specialty;
    private Integer pauseThresholdMinutes;
    private Integer dailyGoalMinutes;
}