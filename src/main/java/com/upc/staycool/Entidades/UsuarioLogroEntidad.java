package com.upc.staycool.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_achievements")
@Getter
@Setter
public class UsuarioLogroEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsuarioEntidad usuario;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false)
    private LogroEntidad logro;

    @Column(name = "unlocked_at", nullable = false, updatable = false)
    private LocalDateTime fechaDesbloqueo = LocalDateTime.now();
}