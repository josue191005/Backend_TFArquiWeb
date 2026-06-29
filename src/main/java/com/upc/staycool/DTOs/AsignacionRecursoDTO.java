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
public class AsignacionRecursoDTO {
    private Long id;
    private Long recursoId;
    private Long pacienteId;
    private Long psicologoId;
    private LocalDateTime fechaAsignacion;
}
