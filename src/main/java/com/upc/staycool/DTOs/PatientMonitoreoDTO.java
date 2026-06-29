package com.upc.staycool.DTOs;

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
    private String ultimaActividad;
    private String tendencia;
    private String profilePictureUrl;
}
