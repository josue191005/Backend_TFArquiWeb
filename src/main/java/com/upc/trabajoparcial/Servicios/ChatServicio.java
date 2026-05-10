package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.ChatDTO;
import com.upc.trabajoparcial.DTOs.MensajeDTO;
import com.upc.trabajoparcial.Entidades.ChatEntidad;
import com.upc.trabajoparcial.Entidades.ChatStatus;
import com.upc.trabajoparcial.Entidades.ChatType;
import com.upc.trabajoparcial.Entidades.MensajeEntidad;
import com.upc.trabajoparcial.Entidades.PacienteAPsicologoEntidad;
import com.upc.trabajoparcial.Entidades.UsuarioEntidad;
import com.upc.trabajoparcial.Repositorios.ChatRepositorio;
import com.upc.trabajoparcial.Repositorios.MensajeRepositorio;
import com.upc.trabajoparcial.Repositorios.PacienteAPsicologoRepositorio;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatServicio {

    @Autowired
    private ChatRepositorio repo;

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PacienteAPsicologoRepositorio pacienteAPsicologoRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ==========================================
    // LO DE TU COMPAÑERO (CRUD BÁSICO DE CHAT)
    // ==========================================
    public ChatDTO crear(ChatDTO dto) {
        ChatEntidad entidad = modelMapper.map(dto, ChatEntidad.class);
        return modelMapper.map(repo.save(entidad), ChatDTO.class);
    }

    public List<ChatDTO> listarTodos() {
        return repo.findAll().stream()
                .map(e -> modelMapper.map(e, ChatDTO.class))
                .collect(Collectors.toList());
    }

    public ChatDTO obtenerPorId(UUID id) {
        ChatEntidad entidad = repo.findById(id).orElse(null);
        return entidad != null ? modelMapper.map(entidad, ChatDTO.class) : null;
    }

    public ChatDTO actualizar(UUID id, ChatDTO dto) {
        ChatEntidad existente = repo.findById(id).orElse(null);
        if (existente != null) {
            existente.setType(dto.getType());
            existente.setStatus(dto.getStatus());
            if (dto.getStatus() != null && dto.getStatus().name().equals("CLOSED") && existente.getClosedAt() == null) {
                existente.setClosedAt(LocalDateTime.now());
            }
            return modelMapper.map(repo.save(existente), ChatDTO.class);
        }
        return null;
    }

    public void eliminar(UUID id) {
        repo.deleteById(id);
    }

    // ==========================================
    // TUS HISTORIAS DE USUARIO (US25 y US07)
    // ==========================================

    // US25: Revisar historial
    public List<MensajeDTO> obtenerHistorial(UUID chatId) {
        return mensajeRepositorio.findByChat_IdOrderByTimestampAsc(chatId)
                .stream()
                .map(m -> modelMapper.map(m, MensajeDTO.class))
                .collect(Collectors.toList());
    }

    // US07: Guardar y emitir por WebSockets
    public MensajeDTO guardarYEnviarMensaje(MensajeDTO dto) {
        ChatEntidad chat = repo.findById(dto.getChat().getId())
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        UsuarioEntidad sender = usuarioRepositorio.findById(dto.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MensajeEntidad nuevoMensaje = new MensajeEntidad();
        nuevoMensaje.setChat(chat);
        nuevoMensaje.setSender(sender);
        nuevoMensaje.setContent(dto.getContent());

        MensajeEntidad guardado = mensajeRepositorio.save(nuevoMensaje);
        MensajeDTO respuesta = modelMapper.map(guardado, MensajeDTO.class);

        // Emitimos en tiempo real
        messagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), respuesta);

        return respuesta;
    }

    public ChatDTO solicitarChatEmergencia(Long patientId) {
        UsuarioEntidad paciente = usuarioRepositorio.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PacienteAPsicologoEntidad relacion = pacienteAPsicologoRepositorio.findFirstByPacienteId(patientId)
                .orElseThrow(() -> new RuntimeException("Psicologo no asignado"));

        ChatEntidad chat = new ChatEntidad();
        chat.setPatient(paciente);
        chat.setPsychologist(relacion.getPsicologo());
        chat.setType(ChatType.EMERGENCY);
        chat.setStatus(ChatStatus.ACTIVE);

        return modelMapper.map(repo.save(chat), ChatDTO.class);
    }
}
