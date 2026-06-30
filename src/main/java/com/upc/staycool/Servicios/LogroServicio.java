package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.LogroDTO;
import com.upc.staycool.Entidades.LogroEntidad;
import com.upc.staycool.Repositorios.LogroRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogroServicio {

    @Autowired
    private LogroRepositorio logroRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public LogroDTO crear(LogroDTO dto) {
        LogroEntidad entidad = modelMapper.map(dto, LogroEntidad.class);
        entidad = logroRepositorio.save(entidad);
        return modelMapper.map(entidad, LogroDTO.class);
    }

    public List<LogroDTO> listarTodos() {
        return logroRepositorio.findAll().stream()
                .map(logro -> modelMapper.map(logro, LogroDTO.class))
                .collect(Collectors.toList());
    }

    public LogroDTO buscarPorId(Long id) {
        LogroEntidad entidad = logroRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado"));
        return modelMapper.map(entidad, LogroDTO.class);
    }

    @Transactional
    public LogroDTO actualizar(Long id, LogroDTO dto) {
        LogroEntidad entidad = logroRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Logro no encontrado"));

        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setPuntosRequeridos(dto.getPuntosRequeridos());
        entidad.setIconoUrl(dto.getIconoUrl());

        entidad = logroRepositorio.save(entidad);
        return modelMapper.map(entidad, LogroDTO.class);
    }

    @Transactional
    public void eliminar(Long id) {
        logroRepositorio.deleteById(id);
    }

    public Long obtenerNuevoLogro(Integer puntos) {
        Long logroId = null;
        int puntosMaximos = -1;

        for (LogroEntidad logro : logroRepositorio.findAll()) {
            if (puntos >= logro.getPuntosRequeridos()) {
                if (logro.getPuntosRequeridos() > puntosMaximos) {
                    logroId = logro.getId();
                    puntosMaximos = logro.getPuntosRequeridos();
                }
            }
        }

        return logroId;
    }

    public Map<String, Object> obtenerEstadoGamificacion(Integer puntos, Integer racha) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("PuntosGanados", puntos);
        respuesta.put("RachaActual", racha != null ? racha : 0);
        respuesta.put("nuevoLogro", obtenerNuevoLogro(puntos));
        return respuesta;
    }
}
