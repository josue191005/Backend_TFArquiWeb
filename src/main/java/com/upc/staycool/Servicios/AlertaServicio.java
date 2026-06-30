package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.AlertaDTO;
import com.upc.staycool.Entidades.AlertaEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.AlertaRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertaServicio {

    @Autowired
    private AlertaRepositorio alertaRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Transactional
    public AlertaDTO crear(AlertaDTO dto) {
        AlertaEntidad entidad = new AlertaEntidad();
        UsuarioEntidad receptor = usuarioRepositorio.findById(dto.getReceptorId())
                .orElseThrow(() -> new RuntimeException("Receptor no encontrado"));
        entidad.setReceptor(receptor);

        if (dto.getEmisorAlertaId() != null) {
            UsuarioEntidad emisor = usuarioRepositorio.findById(dto.getEmisorAlertaId())
                    .orElseThrow(() -> new RuntimeException("Emisor no encontrado"));
            entidad.setEmisorAlerta(emisor);
        }

        entidad.setTipo(dto.getTipo());
        entidad.setMensaje(dto.getMensaje());
        entidad.setLeido(false);entidad.setFechaCreacion(java.time.LocalDateTime.now());

        entidad = alertaRepositorio.save(entidad);
        AlertaDTO respuesta = modelMapper.map(entidad, AlertaDTO.class);
        messagingTemplate.convertAndSend("/topic/alerts/" + receptor.getId(), respuesta);
        return respuesta;
    }

    public List<AlertaDTO> listarTodas() {
        return alertaRepositorio.findAll().stream()
                .map(alerta -> modelMapper.map(alerta, AlertaDTO.class))
                .collect(Collectors.toList());
    }

    public List<AlertaDTO> listarPorReceptor(Long receptorId) {
        return alertaRepositorio.findByReceptor_IdOrderByFechaCreacionDesc(receptorId).stream()
                .map(alerta -> modelMapper.map(alerta, AlertaDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void marcarComoLeida(Long id) {
        AlertaEntidad alerta = alertaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        alerta.setLeido(true);
        alertaRepositorio.save(alerta);
    }
}
