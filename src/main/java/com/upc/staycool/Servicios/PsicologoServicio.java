package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.AlertaDTO;
import com.upc.staycool.DTOs.PsicologoDashboardDTO;
import com.upc.staycool.Entidades.PacienteAPsicologoEntidad;
import com.upc.staycool.Entidades.RegistroDiarioEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.AlertaRepositorio;
import com.upc.staycool.Repositorios.PacienteAPsicologoRepositorio;
import com.upc.staycool.Repositorios.RegistroDiarioRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import com.upc.staycool.Repositorios.UsuarioLogroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class PsicologoServicio {
    @Autowired
    private PacienteAPsicologoRepositorio pacienteRepositorio;

    @Autowired
    private AlertaRepositorio alertaRepositorio;

    @Autowired
    private RegistroDiarioRepositorio registroDiarioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioLogroRepositorio usuarioLogroRepositorio;

    public void asignarPaciente(Long psicologoId, Long pacienteId, String mensajeSolicitud) {
        var psicologo = usuarioRepositorio.findById(psicologoId).orElseThrow();
        var paciente = usuarioRepositorio.findById(pacienteId).orElseThrow();
        
        // Verificar si ya existe una relación
        boolean exists = pacienteRepositorio.existsByPsicologoIdAndPacienteId(psicologoId, pacienteId);
        if (exists) {
            throw new RuntimeException("Ya existe una solicitud o vinculación con este paciente");
        }

        PacienteAPsicologoEntidad relacion = new PacienteAPsicologoEntidad();
        relacion.setPsicologo(psicologo);
        relacion.setPaciente(paciente);
        relacion.setStatus("PENDING");
        relacion.setMensajeSolicitud(mensajeSolicitud);
        pacienteRepositorio.save(relacion);
    }

    public List<com.upc.staycool.DTOs.SolicitudDTO> obtenerSolicitudesPendientes(Long psicologoId) {
        return pacienteRepositorio.findByPsicologoIdAndStatus(psicologoId, "PENDING").stream()
                .map(r -> new com.upc.staycool.DTOs.SolicitudDTO(
                        r.getId(),
                        r.getPaciente().getId(),
                        r.getPaciente().getName(),
                        r.getPaciente().getProfilePictureUrl(),
                        r.getMensajeSolicitud(),
                        r.getFechaAsignacion() != null ? r.getFechaAsignacion().toLocalDate().toString() : LocalDate.now().toString()
                )).collect(Collectors.toList());
    }

    public void aceptarSolicitud(Long solicitudId) {
        PacienteAPsicologoEntidad relacion = pacienteRepositorio.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        relacion.setStatus("ACCEPTED");
        pacienteRepositorio.save(relacion);
    }

    public void rechazarSolicitud(Long solicitudId) {
        PacienteAPsicologoEntidad relacion = pacienteRepositorio.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        relacion.setStatus("REJECTED");
        pacienteRepositorio.save(relacion);
    }
    // US32: Lógica del panel de indicadores
    public PsicologoDashboardDTO obtenerMetricasDashboard(Long psicologoId) {
        Long totalPacientes = pacienteRepositorio.countPacientesByPsicologoId(psicologoId);
        Long alertasPendientes = (long) getCriticalAlerts(psicologoId).size();

        List<PacienteAPsicologoEntidad> relaciones = pacienteRepositorio.findPacientesByPsicologoId(psicologoId);
        List<UsuarioEntidad> pacientes = relaciones.stream()
                .map(PacienteAPsicologoEntidad::getPaciente)
                .collect(Collectors.toList());

        int totalMinutosActivos = 0;
        int totalLogs = 0;
        int sumRedes = 0;
        int sumSueno = 0;
        int sumNocturno = 0;
        int countRedes = 0;

        int[] tendenciaMinutos = new int[5];
        int[] countPorDia = new int[5];
        LocalDate hoy = LocalDate.now();

        for (UsuarioEntidad paciente : pacientes) {
            List<RegistroDiarioEntidad> logs = registroDiarioRepositorio.findByUsuarioIdOrderByFechaRegistroDesc(paciente.getId());
            for (RegistroDiarioEntidad log : logs) {
                if (log.getMinutosActivos() != null) {
                    totalMinutosActivos += log.getMinutosActivos();
                    totalLogs++;
                }
                if (log.getMinutosRedesSociales() != null) {
                    sumRedes += log.getMinutosRedesSociales();
                    countRedes++;
                }
                if (log.getHorasSueno() != null) {
                    sumSueno += log.getHorasSueno();
                }
                if (log.getMinutosUsoNocturno() != null) {
                    sumNocturno += log.getMinutosUsoNocturno();
                }

                long diasDiferencia = java.time.temporal.ChronoUnit.DAYS.between(log.getFechaRegistro(), hoy);
                if (diasDiferencia >= 0 && diasDiferencia < 5) {
                    int index = 4 - (int) diasDiferencia;
                    if (log.getMinutosActivos() != null) {
                        tendenciaMinutos[index] += log.getMinutosActivos();
                        countPorDia[index]++;
                    }
                }
            }
        }

        String promedioString = "0h 0m";
        if (totalLogs > 0) {
            int promedio = totalMinutosActivos / totalLogs;
            promedioString = (promedio / 60) + "h " + (promedio % 60) + "m";
        }

        List<Integer> tendenciaUsoSemanal = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tendenciaUsoSemanal.add(countPorDia[i] > 0 ? (tendenciaMinutos[i] / countPorDia[i]) : 0);
        }

        int promedioRedes = countRedes > 0 ? (sumRedes / countRedes) : 0;
        int promedioSueno = countRedes > 0 ? (sumSueno / countRedes) : 0;
        int promedioNocturno = countRedes > 0 ? (sumNocturno / countRedes) : 0;

        PsicologoDashboardDTO dto = new PsicologoDashboardDTO();
        dto.setTotalPacientes(totalPacientes != null ? totalPacientes : 0L);
        dto.setAlertasPendientes(alertasPendientes != null ? alertasPendientes : 0L);
        dto.setPromedioUsoApp(promedioString);
        dto.setTendenciaUsoSemanal(tendenciaUsoSemanal);
        dto.setPorcentajeUsoRedes(promedioRedes);
        dto.setPorcentajeHorasSueno(promedioSueno);
        dto.setPorcentajeUsoNocturno(promedioNocturno);

        return dto;
    }

    public List<AlertaDTO> getCriticalAlerts(Long psicologoId) {
        return pacienteRepositorio.findPacientesByPsicologoId(psicologoId).stream()
                .map(PacienteAPsicologoEntidad::getPaciente)
                .map(this::crearAlertaCritica)
                .filter(alerta -> alerta != null)
                .collect(Collectors.toList());
    }
    // US10: Monitoreo de pacientes (Armar la lista)
    public java.util.List<com.upc.staycool.DTOs.PatientMonitoreoDTO> monitorearPacientes(Long psicologoId) {
        return pacienteRepositorio.findPacientesByPsicologoId(psicologoId).stream()
                .map(relacion -> {
                    UsuarioEntidad paciente = relacion.getPaciente();
                    String estado = "Estable";
                    String ultimaActividad = "Sin registros";
                    String tendencia = "Estable";

                    // Determinar Estado (CRITICO > OBSERVACION > ESTABLE)
                    AlertaDTO critica = crearAlertaCritica(paciente);
                    
                    List<RegistroDiarioEntidad> logs = registroDiarioRepositorio.findByUsuarioIdOrderByFechaRegistroDesc(paciente.getId());
                    
                    if (critica != null) {
                        estado = "Crítico";
                    } else if (!logs.isEmpty()) {
                        RegistroDiarioEntidad ultimoLog = logs.get(0);
                        if (ultimoLog.getTipoEmocion() != null) {
                            String emocionStr = ultimoLog.getTipoEmocion().toString();
                            if (emocionStr.equals("SAD") || emocionStr.equals("ANXIOUS")) {
                                estado = "En Observación";
                            } else {
                                estado = "Estable";
                            }
                        } else {
                            estado = "Estable";
                        }
                    } else {
                        estado = "Estable";
                    }

                    // Determinar ultima actividad y tendencia
                    if (!logs.isEmpty()) {
                        RegistroDiarioEntidad ultimoLog = logs.get(0);
                        long dias = java.time.temporal.ChronoUnit.DAYS.between(ultimoLog.getFechaRegistro(), LocalDate.now());
                        if (dias == 0) ultimaActividad = "Hoy";
                        else if (dias == 1) ultimaActividad = "Ayer";
                        else ultimaActividad = "Hace " + dias + " días";

                        if (logs.size() >= 2) {
                            Integer hoyMin = logs.get(0).getMinutosActivos();
                            Integer ayerMin = logs.get(1).getMinutosActivos();
                            if (hoyMin != null && ayerMin != null) {
                                if (hoyMin > ayerMin + 30) tendencia = "Ansiedad Alta";
                                else if (hoyMin < ayerMin - 30) tendencia = "Mejora";
                                else tendencia = "Estable";
                            }
                        }
                    }

                    return new com.upc.staycool.DTOs.PatientMonitoreoDTO(
                            paciente.getId(),
                            paciente.getName(),
                            estado,
                            ultimaActividad,
                            tendencia,
                            paciente.getProfilePictureUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    public com.upc.staycool.DTOs.PatientDetailDTO getPatientDetails(Long psicologoId, Long pacienteId) {
        // Validar que el paciente es de este psicologo
        PacienteAPsicologoEntidad relacion = pacienteRepositorio.findByPsicologoId(psicologoId).stream()
                .filter(r -> r.getPaciente().getId().equals(pacienteId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o no pertenece al psicólogo"));

        UsuarioEntidad paciente = relacion.getPaciente();
        com.upc.staycool.DTOs.PatientDetailDTO dto = new com.upc.staycool.DTOs.PatientDetailDTO();
        dto.setPacienteId(paciente.getId());
        dto.setNombre(paciente.getName());
        dto.setEdad(paciente.getAge() != null ? paciente.getAge() : 0);
        dto.setFechaIngreso(paciente.getCreatedAt() != null ? paciente.getCreatedAt().toLocalDate().toString() : LocalDate.now().toString());
        dto.setNotasClinicas(relacion.getNotasClinicas());
        
        AlertaDTO critica = crearAlertaCritica(paciente);
        dto.setEstadoAlerta(critica != null ? "Crítico" : "Estable");

        // Agrupar por dia (últimos 7 días)
        List<RegistroDiarioEntidad> logs = registroDiarioRepositorio.findByUsuarioIdOrderByFechaRegistroDesc(paciente.getId());
        java.util.Map<LocalDate, RegistroDiarioEntidad> maxLogsPerDay = new java.util.HashMap<>();
        List<com.upc.staycool.DTOs.PatientDetailDTO.ActividadEmocional> emoLogs = new java.util.ArrayList<>();
        
        for (RegistroDiarioEntidad log : logs) {
            LocalDate fecha = log.getFechaRegistro();
            if (!maxLogsPerDay.containsKey(fecha)) {
                maxLogsPerDay.put(fecha, log);
            } else {
                // Keep the one with highest active minutes as the "main" metrics for the day
                if (log.getMinutosActivos() != null && (maxLogsPerDay.get(fecha).getMinutosActivos() == null || log.getMinutosActivos() > maxLogsPerDay.get(fecha).getMinutosActivos())) {
                    maxLogsPerDay.put(fecha, log);
                }
            }
            if (log.getTipoEmocion() != null) {
                emoLogs.add(new com.upc.staycool.DTOs.PatientDetailDTO.ActividadEmocional(
                    log.getTipoEmocion().toString(),
                    log.getNotasEmocion(),
                    log.getFechaRegistro().toString()
                ));
            }
        }

        List<Integer> actMin = new java.util.ArrayList<>();
        List<Integer> sleep = new java.util.ArrayList<>();
        List<Integer> redes = new java.util.ArrayList<>();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusDays(i);
            RegistroDiarioEntidad r = maxLogsPerDay.get(d);
            actMin.add(r != null && r.getMinutosActivos() != null ? r.getMinutosActivos() : 0);
            sleep.add(r != null && r.getHorasSueno() != null ? r.getHorasSueno() : 0);
            redes.add(r != null && r.getMinutosRedesSociales() != null ? r.getMinutosRedesSociales() : 0);
        }
        
        dto.setHistorialEmocional(actMin);
        dto.setHistorialHorasSueno(sleep);
        dto.setHistorialUsoRedes(redes);
        dto.setHistorialEmocionalDetallado(emoLogs.stream().limit(10).collect(Collectors.toList()));

        // Actividad Reciente
        List<com.upc.staycool.DTOs.PatientDetailDTO.ActividadReciente> acts = logs.stream().limit(3).map(r -> {
            return new com.upc.staycool.DTOs.PatientDetailDTO.ActividadReciente(
                    "Registro Diario",
                    "Estado reportado: " + (r.getTipoEmocion() != null ? r.getTipoEmocion().toString() : "Ninguno"),
                    r.getFechaRegistro().toString()
            );
        }).collect(Collectors.toList());
        dto.setActividadReciente(acts);

        // Agregamos la racha y logros del usuario
        dto.setRachaActual(paciente.getCurrentStreak() != null ? paciente.getCurrentStreak() : 0);
        
        List<String> logrosStr = usuarioLogroRepositorio.findByUsuarioId(paciente.getId()).stream()
                .map(ul -> ul.getLogro().getNombre())
                .collect(Collectors.toList());
        dto.setLogrosDesbloqueados(logrosStr);
        dto.setMetaDiaria(paciente.getDailyGoalMinutes() != null ? paciente.getDailyGoalMinutes() : 60);

        return dto;
    }

    public void savePatientNotes(Long psicologoId, Long pacienteId, String notes) {
        PacienteAPsicologoEntidad relacion = pacienteRepositorio.findByPsicologoId(psicologoId).stream()
                .filter(r -> r.getPaciente().getId().equals(pacienteId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado o no pertenece al psicólogo"));
        
        relacion.setNotasClinicas(notes);
        pacienteRepositorio.save(relacion);
    }

    private AlertaDTO crearAlertaCritica(UsuarioEntidad paciente) {
        RegistroDiarioEntidad registro = obtenerRegistroDeHoy(paciente);
        int dailyGoal = paciente.getDailyGoalMinutes() != null ? paciente.getDailyGoalMinutes() : 60;

        if (registro == null || registro.getMinutosActivos() == null || registro.getMinutosActivos() <= dailyGoal) {
            return null;
        }

        AlertaDTO alerta = new AlertaDTO();
        alerta.setPatientId(paciente.getId());
        alerta.setAlertLevel("CRITICAL");
        alerta.setExcessMinutes(registro.getMinutosActivos() - dailyGoal);
        alerta.setMensaje("Excedió límite por " + alerta.getExcessMinutes() + " mins");
        alerta.setFechaCreacion(java.time.LocalDateTime.now());
        alerta.setTipo("TIEMPO_EXCEDIDO");
        return alerta;
    }
    
    private RegistroDiarioEntidad obtenerRegistroDeHoy(UsuarioEntidad paciente) {
        return registroDiarioRepositorio
                .findFirstByUsuarioIdAndFechaRegistroOrderByIdDesc(paciente.getId(), LocalDate.now())
                .orElse(null);
    }
}
