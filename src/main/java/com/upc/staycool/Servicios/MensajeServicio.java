package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.MensajeDTO;
import com.upc.staycool.Entidades.ChatEntidad;
import com.upc.staycool.Entidades.MensajeEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.ChatRepositorio;
import com.upc.staycool.Repositorios.MensajeRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MensajeServicio {

    @Autowired
    private MensajeRepositorio repo;

    // Â¡Agregamos los repositorios que faltaban!
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ChatRepositorio chatRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public MensajeDTO crear(MensajeDTO dto) {
        MensajeEntidad entidad = modelMapper.map(dto, MensajeEntidad.class);

        // 1. Asignamos manualmente al SENDER (Usuario)
        if (dto.getSender() != null && dto.getSender().getId() != null) {
            UsuarioEntidad sender = usuarioRepositorio.findById(dto.getSender().getId())
                    .orElseThrow(() -> new RuntimeException("El usuario sender no existe"));
            entidad.setSender(sender);
        } else {
            throw new RuntimeException("El sender es obligatorio para enviar un mensaje");
        }

        // 2. Asignamos manualmente al CHAT (OJO: aquÃ­ asumo que ChatEntidad usa UUID tambiÃ©n)
        if (dto.getChat() != null && dto.getChat().getId() != null) {
            ChatEntidad chat = chatRepositorio.findById(dto.getChat().getId())
                    .orElseThrow(() -> new RuntimeException("El chat no existe"));
            entidad.setChat(chat);
        } else {
            throw new RuntimeException("El chat es obligatorio para enviar un mensaje");
        }

        return modelMapper.map(repo.save(entidad), MensajeDTO.class);
    }

    public List<MensajeDTO> listarTodos() {
        return repo.findAll().stream()
                .map(e -> modelMapper.map(e, MensajeDTO.class))
                .collect(Collectors.toList());
    }

    public MensajeDTO obtenerPorId(UUID id) {
        MensajeEntidad entidad = repo.findById(id).orElse(null);
        return entidad != null ? modelMapper.map(entidad, MensajeDTO.class) : null;
    }

    public List<MensajeDTO> listarPorChat(UUID chatId) {
        return repo.findByChat_IdOrderByTimestampAsc(chatId).stream()
                .map(e -> modelMapper.map(e, MensajeDTO.class))
                .collect(Collectors.toList());
    }

    public MensajeDTO actualizar(UUID id, MensajeDTO dto) {
        MensajeEntidad existente = repo.findById(id).orElse(null);
        if (existente != null) {
            existente.setContent(dto.getContent());
            return modelMapper.map(repo.save(existente), MensajeDTO.class);
        }
        return null;
    }

    public void eliminar(UUID id) {
        repo.deleteById(id);
    }
}