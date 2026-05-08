package com.upc.trabajoparcial.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Table(name = "psychologist_patients")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteAPsicologoEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "psicologo_id")
    private UsuarioEntidad psicologo;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private UsuarioEntidad paciente;

    @CreationTimestamp
    private LocalDateTime fechaAsignacion;
}
