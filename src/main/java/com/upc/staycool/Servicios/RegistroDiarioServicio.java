package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.RegistroDiarioDTO;
import com.upc.staycool.Entidades.RegistroDiarioEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.RegistroDiarioRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import com.upc.staycool.Repositorios.LogroRepositorio;
import com.upc.staycool.Entidades.LogroEntidad;
import com.upc.staycool.Entidades.UsuarioLogroEntidad;
import com.upc.staycool.Repositorios.UsuarioLogroRepositorio;
import com.upc.staycool.Entidades.AlertaEntidad;
import com.upc.staycool.Entidades.PacienteAPsicologoEntidad;
import com.upc.staycool.Repositorios.AlertaRepositorio;
import com.upc.staycool.Repositorios.PacienteAPsicologoRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegistroDiarioServicio {

    @Autowired
    private RegistroDiarioRepositorio registroRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogroRepositorio logroRepositorio;

    @Autowired
    private UsuarioLogroRepositorio usuarioLogroRepositorio;

    @Autowired
    private AlertaServicio alertaServicio;

    @Autowired
    private PacienteAPsicologoRepositorio asignacionRepositorio;

    @PostConstruct
    public void initLogros() {
        asegurarLogro("Maestro del Sueño", "Dormir 8 horas.", 100);
        asegurarLogro("Desintoxicación Digital", "0 mins redes nocturnas.", 100);
        asegurarLogro("Fuego Interior", "Racha de 3 días.", 150);
        asegurarLogro("Fuego Imparable", "Racha de 7 días.", 300);
    }

    private void asegurarLogro(String nombre, String descripcion, Integer puntos) {
        boolean existe = logroRepositorio.findAll().stream().anyMatch(l -> l.getNombre().equals(nombre));
        if (!existe) {
            LogroEntidad l = new LogroEntidad();
            l.setNombre(nombre);
            l.setDescripcion(descripcion);
            l.setPuntosRequeridos(puntos);
            logroRepositorio.save(l);
        }
    }



    // --- US39: Registrar emociones diarias ---
    @org.springframework.transaction.annotation.Transactional
    public RegistroDiarioDTO registrarEmocion(RegistroDiarioDTO dto) {
        // 1. Mapeamos los datos bÃ¡sicos
        RegistroDiarioEntidad entidad = modelMapper.map(dto, RegistroDiarioEntidad.class);

        // 2. FORZAMOS la asignaciÃ³n manual del usuario
        if (dto.getUsuario() != null && dto.getUsuario().getId() != null) {
            UsuarioEntidad usuarioEncontrado = usuarioRepositorio.findById(dto.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuario().getId()));
            entidad.setUsuario(usuarioEncontrado);
        } else {
            throw new RuntimeException("El ID del usuario es obligatorio para registrar una emociÃ³n.");
        }

        // 3. Forzamos la fecha si es que Postman no la estÃ¡ mandando bien
        if (entidad.getFechaRegistro() == null) {
            entidad.setFechaRegistro(java.time.LocalDate.now());
        }

        // 4. Sumamos puntos y gestionamos Racha
        if (entidad.getUsuario() != null) {
            actualizarRachaYPuntos(entidad.getUsuario());
        }

        // 5. Guardamos y devolvemos
        RegistroDiarioEntidad guardado = registroRepositorio.save(entidad);
        return modelMapper.map(guardado, RegistroDiarioDTO.class);
    }

    private void actualizarRachaYPuntos(UsuarioEntidad user) {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        boolean yaDimosPuntosHoy = hoy.equals(user.getLastLogDate());
        
        if (!yaDimosPuntosHoy) {
            user.setTotalPoints((user.getTotalPoints() != null ? user.getTotalPoints() : 0) + 50);
            
            // Lógica de Racha
            if (user.getLastLogDate() == null) {
                user.setCurrentStreak(1);
            } else {
                if (user.getLastLogDate().plusDays(1).equals(hoy)) {
                    user.setCurrentStreak((user.getCurrentStreak() != null ? user.getCurrentStreak() : 0) + 1);
                } else if (user.getLastLogDate().isBefore(hoy.minusDays(1))) {
                    user.setCurrentStreak(1);
                }
            }
            user.setLastLogDate(hoy);
            usuarioRepositorio.save(user);

            // Evaluar insignias de racha
            evaluarInsignia(user, "Fuego Interior", user.getCurrentStreak() >= 3);
            evaluarInsignia(user, "Fuego Imparable", user.getCurrentStreak() >= 7);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public RegistroDiarioDTO guardarMetricasDiarias(RegistroDiarioDTO dto) {
        if (dto.getUsuario() == null || dto.getUsuario().getId() == null) {
            throw new RuntimeException("El ID del usuario es obligatorio para guardar métricas.");
        }
        
        java.time.LocalDate hoy = java.time.LocalDate.now();
        UsuarioEntidad usuarioEncontrado = usuarioRepositorio.findById(dto.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuario().getId()));

        // Busca si ya hay un registro de hoy
        RegistroDiarioEntidad registro = registroRepositorio.findFirstByUsuarioIdAndFechaRegistroOrderByIdDesc(usuarioEncontrado.getId(), hoy)
                .orElse(new RegistroDiarioEntidad());
                
        if (registro.getId() == null) {
            registro.setUsuario(usuarioEncontrado);
            registro.setFechaRegistro(hoy);
        }

        // Actualiza las métricas
        registro.setMinutosActivos(dto.getMinutosActivos() != null ? dto.getMinutosActivos() : 0);
        registro.setMinutosDescanso(dto.getMinutosDescanso() != null ? dto.getMinutosDescanso() : 0);
        registro.setHorasSueno(dto.getHorasSueno() != null ? dto.getHorasSueno() : 0);
        registro.setMinutosRedesSociales(dto.getMinutosRedesSociales() != null ? dto.getMinutosRedesSociales() : 0);
        registro.setMinutosUsoNocturno(dto.getMinutosUsoNocturno() != null ? dto.getMinutosUsoNocturno() : 0);

        Integer meta = usuarioEncontrado.getDailyGoalMinutes() != null ? usuarioEncontrado.getDailyGoalMinutes() : 60;
        registro.setMetaDiariaRedesSociales(meta);

        RegistroDiarioEntidad guardado = registroRepositorio.save(registro);

        actualizarRachaYPuntos(usuarioEncontrado);

        // Evaluamos Hábitos Específicos
        evaluarInsignia(usuarioEncontrado, "Maestro del Sueño", registro.getHorasSueno() >= 8);
        evaluarInsignia(usuarioEncontrado, "Desintoxicación Digital", registro.getMinutosUsoNocturno() == 0 && registro.getMinutosRedesSociales() <= 30);

        // US20: Generar alerta si incumple meta de redes sociales críticamente (ej. más de 50% extra)
        if (registro.getMinutosRedesSociales() > (meta * 1.5)) {
            generarAlertaCritica(usuarioEncontrado, registro.getMinutosRedesSociales());
        }

        return modelMapper.map(guardado, RegistroDiarioDTO.class);
    }

    private void generarAlertaCritica(UsuarioEntidad paciente, int minutosUsados) {
        // Buscar si tiene un psicólogo asignado
        java.util.Optional<PacienteAPsicologoEntidad> asignacionOpt = asignacionRepositorio.findFirstByPacienteId(paciente.getId());
        if (asignacionOpt.isPresent()) {
            UsuarioEntidad psicologo = asignacionOpt.get().getPsicologo();
            
            com.upc.staycool.DTOs.AlertaDTO dto = new com.upc.staycool.DTOs.AlertaDTO();
            dto.setReceptorId(psicologo.getId());
            dto.setEmisorAlertaId(paciente.getId());
            dto.setTipo("USO_CRITICO");
            dto.setMensaje("El paciente " + paciente.getName() + " excedió críticamente su meta diaria con " + minutosUsados + " minutos.");
            alertaServicio.crear(dto);
        }
    }

    private void evaluarInsignia(UsuarioEntidad usuario, String nombreLogro, boolean condicionCumplida) {
        if (!condicionCumplida) return;

        java.util.Optional<LogroEntidad> logroOpt = logroRepositorio.findAll().stream()
                .filter(l -> l.getNombre().equals(nombreLogro)).findFirst();
                
        if (logroOpt.isPresent()) {
            LogroEntidad logro = logroOpt.get();
            boolean yaLoTiene = usuarioLogroRepositorio.findByUsuarioId(usuario.getId()).stream()
                    .anyMatch(ul -> ul.getLogro().getId().equals(logro.getId()));
            if (!yaLoTiene) {
                UsuarioLogroEntidad nuevoLogro = new UsuarioLogroEntidad();
                nuevoLogro.setUsuario(usuario);
                nuevoLogro.setLogro(logro);
                usuarioLogroRepositorio.save(nuevoLogro);

                // Disparar alerta de recompensa desbloqueada al paciente
                com.upc.staycool.DTOs.AlertaDTO alertaRecompensa = new com.upc.staycool.DTOs.AlertaDTO();
                alertaRecompensa.setReceptorId(usuario.getId());
                alertaRecompensa.setTipo("RECOMPENSA");
                alertaRecompensa.setMensaje("¡Recompensa Desbloqueada! " + logro.getNombre() + ": " + logro.getDescripcion());
                alertaServicio.crear(alertaRecompensa);
            }
        }
    }

    // --- US05: Recibir estadÃ­sticas de tiempo ---
    public Map<String, Integer> obtenerEstadisticas(Long usuarioId) {
        Integer totalActivos = registroRepositorio.sumarMinutosActivosPorUsuario(usuarioId);
        Integer totalDescanso = registroRepositorio.sumarMinutosDescansoPorUsuario(usuarioId);

        Map<String, Integer> estadisticas = new HashMap<>();
        estadisticas.put("minutosActivosTotales", totalActivos);
        estadisticas.put("minutosDescansoTotales", totalDescanso);
        return estadisticas;
    }

    // US17: Obtener ultimos 7 dias para Meta Diaria
    public java.util.List<RegistroDiarioDTO> obtenerUltimosRegistros(Long usuarioId) {
        return registroRepositorio.findTop7ByUsuarioIdOrderByFechaRegistroDesc(usuarioId)
                .stream()
                .map(r -> modelMapper.map(r, RegistroDiarioDTO.class))
                .collect(java.util.stream.Collectors.toList());
    }
}
