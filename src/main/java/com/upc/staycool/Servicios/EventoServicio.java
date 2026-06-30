package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.EventoDTO;
import com.upc.staycool.Entidades.EventoEntidad;
import com.upc.staycool.Repositorios.EventoRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventoServicio {

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private ModelMapper modelMapper;

    // Formateador estricto para asegurar compatibilidad total con Google Calendar
    private final DateTimeFormatter googleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss-05:00");

    @Transactional
    public EventoDTO crear(EventoDTO dto) {
        // 1. Persistencia local en PostgreSQL
        EventoEntidad e = new EventoEntidad();
        e.setType(dto.getType());
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setEventDatetime(dto.getEventDatetime());
        e.setEventEndDatetime(dto.getEventEndDatetime());
        e.setLocation(dto.getLocation());
        e.setIsRecurring(dto.getIsRecurring());
        e.setRecurringDays(dto.getRecurringDays());
        e.setUser(
                usuarioRepositorio.findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("El usuario especificado no existe en el sistema"))
        );
        if (dto.getPatientId() != null) {
            e.setPatient(usuarioRepositorio.findById(dto.getPatientId()).orElse(null));
        }

        e = eventoRepositorio.save(e);

        // Se eliminó la asignación "synced-via-web" ya que ahora el callback de OAuth 2.0 lo hará.

        EventoDTO resultado = new EventoDTO();
        resultado.setId(e.getId());
        resultado.setUserId(e.getUser().getId());
        resultado.setType(e.getType());
        resultado.setTitle(e.getTitle());
        resultado.setDescription(e.getDescription());
        resultado.setEventDatetime(e.getEventDatetime());
        resultado.setEventEndDatetime(e.getEventEndDatetime());
        resultado.setLocation(e.getLocation());
        resultado.setIsRecurring(e.getIsRecurring());
        resultado.setRecurringDays(e.getRecurringDays());
        resultado.setGoogleEventId(e.getGoogleEventId());
        resultado.setPatientGoogleEventId(e.getPatientGoogleEventId());
        if (e.getPatient() != null) {
            resultado.setPatientId(e.getPatient().getId());
        }
        
        return resultado;
    }

    public List<EventoDTO> listar() {
        return eventoRepositorio.findAll().stream()
                .map(e -> modelMapper.map(e, EventoDTO.class)).toList();
    }

    public EventoDTO obtener(Long id) {
        return modelMapper.map(eventoRepositorio.findById(id).orElseThrow(), EventoDTO.class);
    }

    @Transactional
    public EventoDTO sincronizarConGoogleManual(Long id) {
        // 1. Buscamos el evento local
        EventoEntidad e = eventoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));

        // 2. ValidaciÃ³n para evitar duplicados
        if (e.getGoogleEventId() != null && !e.getGoogleEventId().trim().isEmpty()) {
            throw new RuntimeException("El evento ya cuenta con un ID de sincronizaciÃ³n activo.");
        }

        // El backend ya no hace nada aquí porque el Frontend llamará a /auth-url/{id} y manejará el popup.
        // Simplemente validamos que exista y devolvemos el DTO.
        return modelMapper.map(e, EventoDTO.class);
    }

    @Transactional
    public void eliminar(Long id) {
        eventoRepositorio.deleteById(id);
    }
}
