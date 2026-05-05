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
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private UsuarioEntidad patient;

    @ManyToOne
    @JoinColumn(name = "psychologist_id")
    private UsuarioEntidad psychologist;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Enumerated(EnumType.STRING)
    private ChatStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime closedAt;
}
