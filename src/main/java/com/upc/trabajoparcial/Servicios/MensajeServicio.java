package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.MensajeDTO;
import com.upc.trabajoparcial.Entidades.MensajeEntidad;
import com.upc.trabajoparcial.Repositorios.MensajeRepositorio;
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

    @Autowired
    private ModelMapper modelMapper;

    public MensajeDTO crear(MensajeDTO dto) {
        MensajeEntidad entidad = modelMapper.map(dto, MensajeEntidad.class);
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

    // Usando la función extra del repositorio
    public List<MensajeDTO> listarPorChat(UUID chatId) {
        return repo.findByChat_IdOrderByTimestampAsc(chatId).stream()
                .map(e -> modelMapper.map(e, MensajeDTO.class))
                .collect(Collectors.toList());
    }

    public MensajeDTO actualizar(UUID id, MensajeDTO dto) {
        MensajeEntidad existente = repo.findById(id).orElse(null);
        if (existente != null) {
            existente.setContent(dto.getContent());
            // En un chat real casi nunca se actualiza el sender o el chat, solo el texto
            return modelMapper.map(repo.save(existente), MensajeDTO.class);
        }
        return null;
    }

    public void eliminar(UUID id) {
        repo.deleteById(id);
    }
}
