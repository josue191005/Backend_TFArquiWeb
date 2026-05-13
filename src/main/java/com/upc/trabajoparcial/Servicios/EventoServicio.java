package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.EventoDTO;
import com.upc.trabajoparcial.Entidades.EventoEntidad;
import com.upc.trabajoparcial.Repositorios.EventoRepositorio;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
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
    private final DateTimeFormatter googleFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Transactional
    public EventoDTO crear(EventoDTO dto) {
        // 1. Persistencia local en PostgreSQL
        EventoEntidad e = modelMapper.map(dto, EventoEntidad.class);
        e.setUser(
                usuarioRepositorio.findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("El usuario especificado no existe en el sistema"))
        );

        e = eventoRepositorio.save(e);

        // 2. Sincronización automática si el flag está activo
        if (dto.isSincronizarConGoogle() && dto.getEventDatetime() != null) {
            try {
                String fechaInicio = dto.getEventDatetime().format(googleFormatter);
                String fechaFin = dto.getEventDatetime().plusHours(1).format(googleFormatter);

                String googleId = googleCalendarService.crearEvento(
                        dto.getTitle(),
                        dto.getDescription(),
                        fechaInicio,
                        fechaFin
                );

                e.setGoogleEventId(googleId);
                e = eventoRepositorio.save(e);

                // ✔️ Mensaje solicitado por el usuario para creación exitosa
                System.out.println("====================================================");
                System.out.println("✅ Evento creado exitosamente en Google Calendar");
                System.out.println("====================================================");

            } catch (Exception ex) {
                System.err.println("⚠️ Advertencia: Falló la creación en Google, pero se guardó localmente: " + ex.getMessage());
            }
        }

        return modelMapper.map(e, EventoDTO.class);
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

        // 2. Validación para evitar duplicados
        if (e.getGoogleEventId() != null) {
            throw new RuntimeException("El evento ya cuenta con un ID de sincronización activo.");
        }

        try {
            // 3. Formateo y envío a la nube
            String fechaInicio = e.getEventDatetime().format(googleFormatter);
            String fechaFin = e.getEventDatetime().plusHours(1).format(googleFormatter);

            String googleId = googleCalendarService.crearEvento(
                    e.getTitle(),
                    e.getDescription(),
                    fechaInicio,
                    fechaFin
            );

            // 4. Actualizamos el registro local con el ID remoto
            e.setGoogleEventId(googleId);
            eventoRepositorio.save(e);

            // ✔️ Mensaje solicitado por el usuario para sincronización manual exitosa
            System.out.println("====================================================");
            System.out.println("🔄 Evento sincronizado exitosamente en Google Calendar");
            System.out.println("====================================================");

        } catch (Exception ex) {
            System.err.println("❌ Fallo crítico en sincronización manual:");
            ex.printStackTrace();
            throw new RuntimeException("Error en la API de Google: " + ex.getMessage());
        }

        return modelMapper.map(e, EventoDTO.class);
    }

    @Transactional
    public void eliminar(Long id) {
        eventoRepositorio.deleteById(id);
    }
}