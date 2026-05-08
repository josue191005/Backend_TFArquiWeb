package com.upc.trabajoparcial.DTOs;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private String name;
    private Integer pauseThresholdMinutes;
    private Integer dailyGoalMinutes;
}