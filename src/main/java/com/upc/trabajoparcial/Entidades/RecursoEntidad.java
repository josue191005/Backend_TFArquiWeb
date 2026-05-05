package com.upc.trabajoparcial.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecursoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private RecursoCategory category;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private UsuarioEntidad uploadedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // getters y setters
}
