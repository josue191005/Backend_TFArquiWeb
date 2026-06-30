package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.RecursoDTO;
import com.upc.staycool.Entidades.RecursoEntidad;
import com.upc.staycool.Repositorios.RecursoRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
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
    private com.upc.staycool.Repositorios.AsignacionRecursoRepositorio asignacionRepositorio;

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

    public List<RecursoDTO> listarRecursosRelajacion() {
        return recursoRepositorio.findAll()
                .stream()
                .filter(r -> r.getCategory().name().equals("MEDITATION")
                        || r.getCategory().name().equals("MUSIC"))
                .map(x -> modelMapper.map(x, RecursoDTO.class))
                .toList();
    }
    public RecursoDTO subirArchivoYCrear(org.springframework.web.multipart.MultipartFile archivo, RecursoDTO dto) {
        try {
            // 1. Subir archivo a la nube (usamos resource_type auto para soportar video/pdf/imagenes)
            java.util.Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), com.cloudinary.utils.ObjectUtils.asMap("resource_type", "auto"));
            String urlObtenida = uploadResult.get("url").toString();

            // 2. Asignar la URL que nos dio la nube al DTO
            dto.setUrl(urlObtenida);

            // 3. Guardar en base de datos usando el mÃ©todo ya existente
            return crear(dto);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error al subir el archivo a Cloud Storage");
        }
    }

    public void assignResourceToPatients(Long recursoId, Long psicologoId, List<Long> patientIds) {
        RecursoEntidad recurso = recursoRepositorio.findById(recursoId)
            .orElseThrow(() -> new RuntimeException("Recurso no existe"));
        com.upc.staycool.Entidades.UsuarioEntidad psicologo = usuarioRepositorio.findById(psicologoId)
            .orElseThrow(() -> new RuntimeException("Psicologo no existe"));

        List<com.upc.staycool.Entidades.AsignacionRecursoEntidad> currentAssignments = asignacionRepositorio.findByRecursoId(recursoId);

        for (com.upc.staycool.Entidades.AsignacionRecursoEntidad asignacion : currentAssignments) {
            if (!patientIds.contains(asignacion.getPaciente().getId())) {
                asignacionRepositorio.delete(asignacion);
            }
        }

        for (Long pacienteId : patientIds) {
            if (!asignacionRepositorio.existsByRecursoIdAndPacienteId(recursoId, pacienteId)) {
                com.upc.staycool.Entidades.UsuarioEntidad paciente = usuarioRepositorio.findById(pacienteId)
                    .orElseThrow(() -> new RuntimeException("Paciente no existe"));
                com.upc.staycool.Entidades.AsignacionRecursoEntidad asignacion = new com.upc.staycool.Entidades.AsignacionRecursoEntidad();
                asignacion.setRecurso(recurso);
                asignacion.setPaciente(paciente);
                asignacion.setPsicologo(psicologo);
                asignacionRepositorio.save(asignacion);
            }
        }
    }

    public List<RecursoDTO> getAssignedResourcesForPatient(Long pacienteId) {
        return asignacionRepositorio.findByPacienteId(pacienteId)
            .stream()
            .map(com.upc.staycool.Entidades.AsignacionRecursoEntidad::getRecurso)
            .map(r -> modelMapper.map(r, RecursoDTO.class))
            .toList();
    }

    public List<Long> getAssignedPatientIds(Long recursoId) {
        return asignacionRepositorio.findByRecursoId(recursoId).stream()
                .map(asignacion -> asignacion.getPaciente().getId())
                .collect(java.util.stream.Collectors.toList());
    }
}