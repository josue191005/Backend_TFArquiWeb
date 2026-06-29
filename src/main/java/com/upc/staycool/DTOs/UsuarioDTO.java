package com.upc.staycool.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un formato de email vÃ¡lido")
    private String email;

    private Integer age;

    @NotBlank(message = "La contraseÃ±a es obligatoria")
    private String passwordHash;

    // AquÃ­ pedimos solo el ID del rol al momento de hacer el POST en Postman
    @NotNull(message = "El ID del rol es obligatorio")
    private Long rolId;

    private Integer pauseThresholdMinutes;
    private Integer dailyGoalMinutes;
    private Integer totalPoints;
    private String specialty;
    private String clinicName;
    private String profilePictureUrl;
    private LocalDateTime createdAt;
}
