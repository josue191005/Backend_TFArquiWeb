package com.upc.staycool.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PsicologoDashboardDTO {
    private Long totalPacientes;
    private Long alertasPendientes;

    // Métricas Agregadas
    private String promedioUsoApp; // Ej: "4h 15m"

    // Tendencias Semanales (Los 5 días de la semana, de Lunes a Viernes o últimos 5 días)
    private List<Integer> tendenciaUsoSemanal;
    
    // Tendencias Bienestar (Porcentajes promedio de los pacientes)
    private Integer porcentajeUsoRedes;
    private Integer porcentajeHorasSueno;
    private Integer porcentajeUsoNocturno;
}
