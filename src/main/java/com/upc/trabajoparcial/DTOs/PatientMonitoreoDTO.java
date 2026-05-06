package com.upc.trabajoparcial.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientMonitoreoDTO {
    private Long pacienteId;
    private String nombrePaciente;
    private String estado;
}
