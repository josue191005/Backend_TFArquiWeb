package com.upc.staycool.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Daily_logs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroDiarioEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntidad usuario;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @Column(columnDefinition = "integer default 0 ")
    private Integer minutosActivos;

    @Column(columnDefinition = "integer default 0 ")
    private Integer minutosDescanso;

    @Enumerated(EnumType.STRING)
    private EmotionType tipoEmocion;

    @Column(length = 500)
    private String notasEmocion;

    // Nuevas métricas de Bienestar
    @Column(columnDefinition = "integer default 0")
    private Integer horasSueno;

    @Column(columnDefinition = "integer default 0")
    private Integer minutosRedesSociales;

    @Column(columnDefinition = "integer default 0")
    private Integer minutosUsoNocturno;

    @Column(columnDefinition = "integer default 60")
    private Integer metaDiariaRedesSociales;

    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;

}
