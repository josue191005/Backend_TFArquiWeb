package com.upc.staycool.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailDTO {
    private Long pacienteId;
    private String nombre;
    private Integer edad; // Mocked or calculated
    private String fechaIngreso;
    private String estadoAlerta;
    private String notasClinicas;
    private List<Integer> historialEmocional; // minutes active
    private List<Integer> historialHorasSueno;
    private List<Integer> historialUsoRedes;
    private List<ActividadReciente> actividadReciente;
    private List<ActividadEmocional> historialEmocionalDetallado;
    private Integer rachaActual;
    private List<String> logrosDesbloqueados;
    private Integer metaDiaria;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActividadEmocional {
        private String emocion;
        private String notas;
        private String fecha;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActividadReciente {
        private String titulo;
        private String descripcion;
        private String fechaHora; // "Hoy, 14:30"
    }
}
