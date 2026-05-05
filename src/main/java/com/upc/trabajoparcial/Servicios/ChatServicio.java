package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.ChatDTO;
import com.upc.trabajoparcial.Entidades.ChatEntidad;
import com.upc.trabajoparcial.Repositorios.ChatRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ModelMapper modelMapper;

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

            // Lógica extra: Si se cambia el estado a CLOSED, guardamos la fecha exacta
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
}
