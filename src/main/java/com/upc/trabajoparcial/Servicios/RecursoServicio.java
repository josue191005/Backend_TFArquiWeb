package com.upc.trabajoparcial.Servicios;

import com.upc.trabajoparcial.DTOs.RecursoDTO;
import com.upc.trabajoparcial.Entidades.RecursoEntidad;
import com.upc.trabajoparcial.Repositorios.RecursoRepositorio;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoServicio {

    @Autowired
    private RecursoRepositorio recursoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private com.cloudinary.Cloudinary cloudinary;

    public RecursoDTO crear(RecursoDTO dto) {
        RecursoEntidad r = modelMapper.map(dto, RecursoEntidad.class);

        if (dto.getUploadedById() != null) {
            r.setUploadedBy(
                    usuarioRepositorio.findById(dto.getUploadedById())
                            .orElseThrow(() -> new RuntimeException("Usuario no existe"))
            );
        }

        return modelMapper.map(recursoRepositorio.save(r), RecursoDTO.class);
    }

    public List<RecursoDTO> listar() {
        return recursoRepositorio.findAll()
                .stream()
                .map(r -> modelMapper.map(r, RecursoDTO.class))
                .toList();
    }

    public RecursoDTO obtener(Long id) {
        return modelMapper.map(
                recursoRepositorio.findById(id).orElseThrow(),
                RecursoDTO.class
        );
    }

    public void eliminar(Long id) {
        recursoRepositorio.deleteById(id);
    }

    public RecursoDTO subirArchivoYCrear(org.springframework.web.multipart.MultipartFile archivo, RecursoDTO dto) {
        try {
            // 1. Subir archivo a la nube
            java.util.Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), com.cloudinary.utils.ObjectUtils.emptyMap());
            String urlObtenida = uploadResult.get("url").toString();

            // 2. Asignar la URL que nos dio la nube al DTO
            dto.setUrl(urlObtenida);

            // 3. Guardar en base de datos usando el método ya existente
            return crear(dto);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloud Storage");
        }
    }
}