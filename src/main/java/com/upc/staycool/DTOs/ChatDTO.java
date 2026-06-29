package com.upc.staycool.DTOs;

import com.upc.staycool.Entidades.ChatStatus;
import com.upc.staycool.Entidades.ChatType;
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
public class ChatDTO {

    private UUID id;
    private UsuarioDTO patient;
    private UsuarioDTO psychologist;
    private ChatType type;
    private ChatStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
}
