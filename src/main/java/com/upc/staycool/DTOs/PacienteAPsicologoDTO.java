package com.upc.staycool.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteAPsicologoDTO {
    private Long id;
    private UsuarioDTO psicologo;
    private UsuarioDTO paciente;
    private String status;
    private LocalDateTime requestedAt;
}