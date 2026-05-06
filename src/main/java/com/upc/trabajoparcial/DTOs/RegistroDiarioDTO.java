package com.upc.trabajoparcial.DTOs;

import com.upc.trabajoparcial.Entidades.EmotionType;
import com.upc.trabajoparcial.Entidades.UsuarioEntidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegistroDiarioDTO {

    private Long id;
    private UsuarioEntidad usuario;
    private LocalDate fechaRegistro;
    private Integer minutosActivos;
    private Integer minutosDescanso;
    private EmotionType tipoEmocion;
    private String notasEmocion;
    private LocalDateTime ultimaActualizacion;
}
