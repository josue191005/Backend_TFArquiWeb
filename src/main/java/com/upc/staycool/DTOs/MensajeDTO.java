package com.upc.staycool.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MensajeDTO {

    private UUID id;
    private ChatDTO chat;
    private UsuarioDTO sender;
    @NotBlank
    private String content;
    private LocalDateTime timestamp;
}
