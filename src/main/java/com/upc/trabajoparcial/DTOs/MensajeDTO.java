package com.upc.trabajoparcial.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensajeDTO {

    private UUID id;
    private ChatDTO chat;
    private UsuarioDTO sender;
    private String content;
    private LocalDateTime timestamp;
}
