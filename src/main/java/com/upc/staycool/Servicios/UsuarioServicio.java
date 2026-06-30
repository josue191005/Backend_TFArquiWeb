package com.upc.staycool.Servicios;

import com.upc.staycool.DTOs.UsuarioDTO;
import com.upc.staycool.Entidades.RolEntidad;
import com.upc.staycool.Entidades.UsuarioEntidad;
import com.upc.staycool.Repositorios.RolRepositorio;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private RolRepositorio rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private com.cloudinary.Cloudinary cloudinary;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        UsuarioEntidad usuario = modelMapper.map(usuarioDTO, UsuarioEntidad.class);

        // Buscamos el rol en la base de datos y se lo asignamos al usuariohhhhhhhhh
        RolEntidad rol = rolRepository.findById(usuarioDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRolEntidad(rol);

        if(usuario.getTotalPoints() == null) {
            usuario.setTotalPoints(0);
        }

        usuario = usuarioRepository.save(usuario);
        return mapToDTO(usuario);
    }

    public UsuarioDTO updateProfile(Long id, com.upc.staycool.DTOs.UsuarioUpdateDTO updateDTO) {
        // 1. Busca al usuario existente
        UsuarioEntidad usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // 2. Actualiza SOLO los campos permitidos si no vienen nulos o vacÃ­os
        if (updateDTO.getName() != null && !updateDTO.getName().trim().isEmpty()) {
            usuario.setName(updateDTO.getName());
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().trim().isEmpty()) {
            usuario.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getAge() != null) {
            usuario.setAge(updateDTO.getAge());
        }
        if (updateDTO.getSpecialty() != null) {
            usuario.setSpecialty(updateDTO.getSpecialty());
        }
        if (updateDTO.getPauseThresholdMinutes() != null) {
            usuario.setPauseThresholdMinutes(updateDTO.getPauseThresholdMinutes());
        }
        if (updateDTO.getDailyGoalMinutes() != null) {
            usuario.setDailyGoalMinutes(updateDTO.getDailyGoalMinutes());
        }

        // 3. Guarda los cambios.
        usuario = usuarioRepository.save(usuario);

        // 4. Retorna el DTO actualizado
        return mapToDTO(usuario);
    }

    public UsuarioDTO uploadProfilePicture(Long id, org.springframework.web.multipart.MultipartFile archivo) {
        UsuarioEntidad usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        try {
            java.util.Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(), com.cloudinary.utils.ObjectUtils.asMap("resource_type", "image"));
            String urlObtenida = uploadResult.get("url").toString();
            usuario.setProfilePictureUrl(urlObtenida);
            return mapToDTO(usuarioRepository.save(usuario));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary");
        }
    }

    public UsuarioDTO deleteProfilePicture(Long id) {
        UsuarioEntidad usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setProfilePictureUrl(null);
        return mapToDTO(usuarioRepository.save(usuario));
    }

    public void changePassword(Long id, com.upc.staycool.DTOs.UsuarioPasswordDTO passwordDTO) {
        UsuarioEntidad usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(passwordDTO.getNewPassword()));
        usuarioRepository.save(usuario);
    }

    public void guardarMetaDiaria(Long usuarioId, Integer targetMinutes) {
        UsuarioEntidad usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        usuario.setDailyGoalMinutes(targetMinutes);
        usuarioRepository.save(usuario);
    }

    public Integer obtenerPuntos(Long usuarioId) {
        UsuarioEntidad usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        return usuario.getTotalPoints() != null ? usuario.getTotalPoints() : 0;
    }

    public List<UsuarioDTO> getPsychologistsDirectory() {
        return usuarioRepository.findByRolEntidad_Name("PSICOLOGO").stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> listAll() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO getById(Long id) {
        UsuarioEntidad usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToDTO(usuario);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        UsuarioEntidad usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setName(usuarioDTO.getName());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPasswordHash(usuarioDTO.getPasswordHash());
        usuario.setPauseThresholdMinutes(usuarioDTO.getPauseThresholdMinutes());
        usuario.setDailyGoalMinutes(usuarioDTO.getDailyGoalMinutes());
        usuario.setTotalPoints(usuarioDTO.getTotalPoints());

        // Actualizamos el rol si es necesario
        RolEntidad rol = rolRepository.findById(usuarioDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRolEntidad(rol);

        usuario = usuarioRepository.save(usuario);
        return mapToDTO(usuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(UsuarioEntidad usuario) {
        UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
        if (usuario.getRolEntidad() != null) {
            dto.setRolId(usuario.getRolEntidad().getId());
        }
        return dto;
    }
}
