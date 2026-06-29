package com.upc.staycool.DTOs;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {
    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String name;
}