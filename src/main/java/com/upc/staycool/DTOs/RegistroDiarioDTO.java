package com.upc.staycool.DTOs;

import com.upc.staycool.Entidades.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDiarioDTO {
    private Long id;
    private UsuarioDTO usuario;

    // Nombres exactos a la Entidad para que ModelMapper funcione
    private EmotionType tipoEmocion;
    private String notasEmocion;
    private LocalDate fechaRegistro;

    // Agregamos estos para que las estadÃ­sticas funcionen luego
    private Integer minutosActivos;
    private Integer minutosDescanso;
    private Integer horasSueno;
    private Integer minutosRedesSociales;
    private Integer minutosUsoNocturno;
    private Integer metaDiariaRedesSociales;
}