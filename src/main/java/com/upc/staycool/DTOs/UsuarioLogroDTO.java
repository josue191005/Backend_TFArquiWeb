package com.upc.staycool.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioLogroDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID del logro es obligatorio")
    private Long logroId;

    private LocalDateTime fechaDesbloqueo;
}