package com.upc.trabajoparcial.DTOs;

import com.upc.trabajoparcial.Entidades.UsuarioEntidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteAPsicologoDTO {

    private Long id;
    private UsuarioEntidad psicologo;
    private UsuarioEntidad paciente;
    private LocalDateTime fechaAsignacion;
}
