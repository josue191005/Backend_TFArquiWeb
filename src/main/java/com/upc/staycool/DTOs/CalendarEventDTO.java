package com.upc.staycool.DTOs;

import lombok.Data;

@Data
public class CalendarEventDTO {
    private String summary;    // TÃ­tulo de la reuniÃ³n
    private String startTime;  // Formato: 2026-05-10T10:00:00Z
    private String endTime;    // Formato: 2026-05-10T11:00:00Z
}