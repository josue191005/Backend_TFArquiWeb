package com.upc.staycool.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogroDTO {

    private Long id;

    @NotBlank(message = "El nombre del logro es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Debe especificar los puntos requeridos")
    @Min(value = 1, message = "Los puntos deben ser mayores a 0")
    private Integer puntosRequeridos;

    private String iconoUrl;
}