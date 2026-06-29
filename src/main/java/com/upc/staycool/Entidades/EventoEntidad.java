package com.upc.staycool.Entidades;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
public class EventoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsuarioEntidad user;

    @Enumerated(EnumType.STRING)
    private EventoType type;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDatetime;

    @Column(columnDefinition = "boolean default false")
    private Boolean isRecurring;

    private String googleEventId;

    private String patientGoogleEventId;

    private LocalDateTime eventEndDatetime;

    private String location;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private UsuarioEntidad patient;

    private String recurringDays;

}
