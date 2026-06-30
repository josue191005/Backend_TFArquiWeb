package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.ChatDTO;
import com.upc.staycool.DTOs.MensajeDTO;
import com.upc.staycool.DTOs.UsuarioDTO;
import com.upc.staycool.Entidades.ChatEntidad;
import com.upc.staycool.Entidades.ChatStatus;
import com.upc.staycool.Entidades.ChatType;
import com.upc.staycool.Entidades.MensajeEntidad;
import com.upc.staycool.Entidades.PacienteAPsicologoEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.ChatRepositorio;
import com.upc.staycool.Repositorios.MensajeRepositorio;
import com.upc.staycool.Repositorios.PacienteAPsicologoRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
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

    @Autowired
    private AlertaServicio alertaServicio;

    // ==========================================
    // LO DE TU COMPAÃ‘ERO (CRUD BÃSICO DE CHAT)
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

    public List<ChatDTO> listarMisChats(Long userId, String role) {
        if ("ROLE_PATIENT".equals(role)) {
            List<ChatEntidad> chats = new java.util.ArrayList<>(repo.findByPatient_Id(userId));
            
            // Auto-generar chat si el paciente tiene un psicólogo asignado y no hay chat
            PacienteAPsicologoEntidad relacion = pacienteAPsicologoRepositorio.findFirstByPacienteId(userId).orElse(null);
            if (relacion != null && "ACCEPTED".equals(relacion.getStatus())) {
                boolean hasChat = chats.stream().anyMatch(c -> c.getPsychologist().getId().equals(relacion.getPsicologo().getId()));
                if (!hasChat) {
                    ChatEntidad newChat = new ChatEntidad();
                    newChat.setPatient(relacion.getPaciente());
                    newChat.setPsychologist(relacion.getPsicologo());
                    newChat.setType(ChatType.STANDARD);
                    newChat.setStatus(ChatStatus.ACTIVE);
                    ChatEntidad saved = repo.save(newChat);
                    chats.add(saved);
                }
            }
            
            return chats.stream()
                    .map(e -> modelMapper.map(e, ChatDTO.class))
                    .collect(Collectors.toList());
        } else if ("ROLE_PSYCHOLOGIST".equals(role)) {
            List<ChatEntidad> chats = new java.util.ArrayList<>(repo.findByPsychologist_Id(userId));
            
            // Auto-generar chats faltantes para pacientes asignados (ACCEPTED)
            List<PacienteAPsicologoEntidad> pacientesAsignados = pacienteAPsicologoRepositorio.findPacientesByPsicologoId(userId);
            for (PacienteAPsicologoEntidad relacion : pacientesAsignados) {
                boolean hasChat = chats.stream().anyMatch(c -> c.getPatient().getId().equals(relacion.getPaciente().getId()));
                if (!hasChat) {
                    ChatEntidad newChat = new ChatEntidad();
                    newChat.setPatient(relacion.getPaciente());
                    newChat.setPsychologist(relacion.getPsicologo());
                    newChat.setType(ChatType.STANDARD);
                    newChat.setStatus(ChatStatus.ACTIVE);
                    ChatEntidad saved = repo.save(newChat);
                    chats.add(saved);
                }
            }
            
            return chats.stream()
                    .map(e -> modelMapper.map(e, ChatDTO.class))
                    .collect(Collectors.toList());
        }
        return List.of();
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

        List<ChatEntidad> chats = repo.findByPatient_Id(patientId);
        ChatEntidad targetChat = chats.stream()
                .filter(c -> c.getPsychologist().getId().equals(relacion.getPsicologo().getId()))
                .findFirst()
                .orElseGet(() -> {
                    ChatEntidad newChat = new ChatEntidad();
                    newChat.setPatient(paciente);
                    newChat.setPsychologist(relacion.getPsicologo());
                    newChat.setType(ChatType.STANDARD);
                    newChat.setStatus(ChatStatus.ACTIVE);
                    return repo.save(newChat);
                });

        ChatDTO chatDto = modelMapper.map(targetChat, ChatDTO.class);

        MensajeDTO msgDto = new MensajeDTO();
        msgDto.setContent("🚨 S.O.S - Necesito ayuda urgente.");
        UsuarioDTO senderDto = new UsuarioDTO();
        senderDto.setId(patientId);
        msgDto.setSender(senderDto);
        msgDto.setChat(chatDto);
        guardarYEnviarMensaje(msgDto);

        // Crear alerta S.O.S para el psicólogo
        com.upc.staycool.DTOs.AlertaDTO alertaDTO = new com.upc.staycool.DTOs.AlertaDTO();
        alertaDTO.setReceptorId(relacion.getPsicologo().getId());
        alertaDTO.setEmisorAlertaId(patientId);
        alertaDTO.setTipo("EMERGENCIA");
        alertaDTO.setMensaje("🚨 S.O.S: El paciente " + paciente.getName() + " ha solicitado ayuda inmediata.");
        alertaServicio.crear(alertaDTO);

        return chatDto;
    }
}
