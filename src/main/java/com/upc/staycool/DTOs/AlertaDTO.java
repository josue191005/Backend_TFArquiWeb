package com.upc.staycool.DTOs;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AlertaDTO {
    private Long id;
    private Long receptorId;
    private Long emisorAlertaId;
    private String tipo;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fechaCreacion;
    private Long patientId;
    private String alertLevel;
    private Integer excessMinutes;
}
