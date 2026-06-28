package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.RegistroDiarioDTO;
import com.upc.staycool.DTOs.UsuarioUpdateDTO;
import com.upc.staycool.Servicios.LogroServicio;
import com.upc.staycool.Servicios.RegistroDiarioServicio;
import com.upc.staycool.Servicios.UsuarioLogroServicio;
import com.upc.staycool.Servicios.UsuarioServicio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/emotions")
public class RegistroDiarioControlador {

    // 1. Agrupamos todas las inyecciones de dependencias al inicio
    @Autowired
    private RegistroDiarioServicio servicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private LogroServicio logroServicio;

    @Autowired
    private UsuarioLogroServicio usuarioLogroServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // US39: POST /api/v1/emotions/log
    @PostMapping("/log")
    public RegistroDiarioDTO registrarEmocion(@RequestBody RegistroDiarioDTO dto) {
        return servicio.registrarEmocion(dto);
    }

    @PostMapping("/metrics")
    public RegistroDiarioDTO guardarMetricas(@RequestBody RegistroDiarioDTO dto) {
        return servicio.guardarMetricasDiarias(dto);
    }

    // US05: GET /api/v1/emotions/statistics/usage
    @GetMapping("/statistics/usage")
    public Map<String, Integer> obtenerEstadisticasTiempo(@RequestParam Long usuarioId) {
        return servicio.obtenerEstadisticas(usuarioId);
    }

    @PostMapping("/goals/daily")
    public Map<String, String> guardarMetaDiaria(
            @RequestParam Long usuarioId,
            @RequestBody UsuarioUpdateDTO body) {

        usuarioServicio.guardarMetaDiaria(usuarioId, body.getDailyGoalMinutes());
        return Map.of("message", "Meta guardada");
    }

    // GET /api/v1/emotions/goals/daily
    @GetMapping("/goals/daily")
    public Map<String, Integer> obtenerMetaDiaria(@RequestParam Long usuarioId) {
        var user = usuarioRepositorio.findById(usuarioId).orElseThrow();
        return Map.of("dailyGoalMinutes", user.getDailyGoalMinutes() != null ? user.getDailyGoalMinutes() : 60);
    }

    // GET /api/v1/emotions/gamification/status
    @GetMapping("/gamification/status")
    public Map<String, Object> obtenerEstadoGamificacion(@RequestParam Long usuarioId) {
        var user = usuarioRepositorio.findById(usuarioId).orElseThrow();
        Integer puntos = user.getTotalPoints() != null ? user.getTotalPoints() : 0;
        Integer racha = user.getCurrentStreak() != null ? user.getCurrentStreak() : 0;
        return logroServicio.obtenerEstadoGamificacion(puntos, racha);
    }

    // GET /api/v1/emotions/users/{userId}/achievements
    @GetMapping("/users/{userId}/achievements")
    public Map<String, Object> listarLogrosUsuario(@PathVariable Long userId) {
        return usuarioLogroServicio.listarLogrosPorUsuario(userId);
    }

    // GET /api/v1/emotions/logs/{usuarioId}
    @GetMapping("/logs/{usuarioId}")
    public java.util.List<RegistroDiarioDTO> obtenerUltimosRegistros(@PathVariable Long usuarioId) {
        return servicio.obtenerUltimosRegistros(usuarioId);
    }
}