package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.UsuarioLogroDTO;
import com.upc.trabajoparcial.Entidades.LogroEntidad;
import com.upc.trabajoparcial.Entidades.UsuarioLogroEntidad;
import com.upc.trabajoparcial.Repositorios.LogroRepositorio;
import com.upc.trabajoparcial.Repositorios.UsuarioLogroRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioLogroServicio {

    @Autowired
    private UsuarioLogroRepositorio usuarioLogroRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogroRepositorio logroRepositorio;

    @Transactional
    public UsuarioLogroDTO crear(UsuarioLogroDTO dto) {
        UsuarioLogroEntidad entidad = modelMapper.map(dto, UsuarioLogroEntidad.class);
        entidad = usuarioLogroRepositorio.save(entidad);
        return modelMapper.map(entidad, UsuarioLogroDTO.class);
    }

    public List<UsuarioLogroDTO> listarTodos() {
        return usuarioLogroRepositorio.findAll().stream()
                .map(ul -> modelMapper.map(ul, UsuarioLogroDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Long id) {
        usuarioLogroRepositorio.deleteById(id);
    }

    public Map<String, Object> listarLogrosPorUsuario(Long usuarioId) {
        java.util.Set<Long> idsDesbloqueados = usuarioLogroRepositorio.findByUsuarioId(usuarioId).stream()
                .map(item -> item.getLogro().getId())
                .collect(Collectors.toSet());

        java.util.Map<Boolean, java.util.List<java.util.Map<String, Object>>> parts = logroRepositorio.findAll().stream()
                .map(logro -> crearDatoLogro(logro, idsDesbloqueados.contains(logro.getId())))
                .collect(Collectors.partitioningBy(m -> (Boolean) m.get("unlocked")));

        java.util.List<java.util.Map<String, Object>> achievements = parts.getOrDefault(Boolean.TRUE, java.util.List.of());
        java.util.List<java.util.Map<String, Object>> pending = parts.getOrDefault(Boolean.FALSE, java.util.List.of());

        return java.util.Map.of("achievements", achievements, "pending", pending);
    }

    private Map<String, Object> crearDatoLogro(LogroEntidad logro, boolean unlocked) {
        Map<String, Object> dato = new LinkedHashMap<>();
        dato.put("name", logro.getNombre());
        dato.put("unlocked", unlocked);
        return dato;
    }
}
