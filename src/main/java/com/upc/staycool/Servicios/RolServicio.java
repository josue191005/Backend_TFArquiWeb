package com.upc.staycool.Servicios;
import com.upc.staycool.DTOs.RolDTO;
import com.upc.staycool.Entidades.RolEntidad;
import com.upc.staycool.Repositorios.RolRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolServicio {

    @Autowired
    private RolRepositorio rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RolDTO create(RolDTO rolDTO) {
        RolEntidad rol = modelMapper.map(rolDTO, RolEntidad.class);
        rol = rolRepository.save(rol);
        return modelMapper.map(rol, RolDTO.class);
    }

    public List<RolDTO> listAll() {
        return rolRepository.findAll().stream()
                .map(rol -> modelMapper.map(rol, RolDTO.class))
                .collect(Collectors.toList());
    }

    public RolDTO update(Long id, RolDTO rolDTO) {
        RolEntidad rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setName(rolDTO.getName());
        rol = rolRepository.save(rol);
        return modelMapper.map(rol, RolDTO.class);
    }

    public void delete(Long id) {
        rolRepository.deleteById(id);
    }
}