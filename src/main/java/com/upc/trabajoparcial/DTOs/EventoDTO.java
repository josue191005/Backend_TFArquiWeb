package com.upc.trabajoparcial.DTOs;

import com.upc.trabajoparcial.Entidades.EventoType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private Long id;
    private Long userId;
    private EventoType type;
    private String title;
    private String description;
    private LocalDateTime eventDatetime;
    private Boolean isRecurring;
    private String googleEventId;

}
