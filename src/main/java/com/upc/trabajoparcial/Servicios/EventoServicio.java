package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.EventoDTO;
import com.upc.trabajoparcial.Entidades.EventoEntidad;
import com.upc.trabajoparcial.Repositorios.EventoRepositorio;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoServicio {

    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    public EventoDTO crear(EventoDTO dto) {
        EventoEntidad e = modelMapper.map(dto, EventoEntidad.class);

        // ATENCIÓN AQUÍ: Si getUserId() te sigue saliendo en rojo,
        // bórralo, escribe "dto.get" y deja que IntelliJ te autocomplete
        // con el nombre correcto (seguro es getIdUsuario() o similar).
        e.setUser(
                usuarioRepositorio.findById(dto.getUserId())
                        .orElseThrow(() -> new RuntimeException("Usuario no existe"))
        );

        return modelMapper.map(eventoRepositorio.save(e), EventoDTO.class);
    }

    public List<EventoDTO> listar() {
        return eventoRepositorio.findAll()
                .stream()
                .map(e -> modelMapper.map(e, EventoDTO.class))
                .toList();
    }

    public EventoDTO obtener(Long id) {
        return modelMapper.map(
                eventoRepositorio.findById(id).orElseThrow(),
                EventoDTO.class
        );
    }

    public void eliminar(Long id) {
        eventoRepositorio.deleteById(id);
    }
}