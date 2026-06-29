package com.upc.staycool.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
public class AlertaEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // AquÃ­ usamos la clase que ya creÃ³ JosuÃ©
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UsuarioEntidad receptor;

    @ManyToOne
    @JoinColumn(name = "trigger_user_id")
    private UsuarioEntidad emisorAlerta;

    @Column(nullable = false)
    private String tipo; // PAUSA_DIGITAL, USO_CRITICO, EMERGENCIA

    @Column(nullable = false)
    private String mensaje;

    @Column(name = "is_read")
    private Boolean leido = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}