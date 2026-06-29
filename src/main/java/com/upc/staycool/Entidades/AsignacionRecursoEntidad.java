package com.upc.staycool.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionRecursoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recurso_id", nullable = false)
    private RecursoEntidad recurso;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private UsuarioEntidad paciente;

    @ManyToOne
    @JoinColumn(name = "psicologo_id")
    private UsuarioEntidad psicologo;

    @CreationTimestamp
    private LocalDateTime fechaAsignacion;
}
