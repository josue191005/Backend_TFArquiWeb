package com.upc.staycool.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteFotoUrl;
    private String mensaje;
    private String fecha;
}
