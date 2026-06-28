package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.UsuarioDTO;
import com.upc.staycool.Repositorios.UsuarioRepositorio;
import com.upc.staycool.Servicios.UsuarioServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upc.staycool.DTOs.UsuarioUpdateDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return new ResponseEntity<>(usuarioService.create(usuarioDTO), HttpStatus.CREATED);
    }

    @GetMapping("/psychologists/directory")
    public ResponseEntity<List<UsuarioDTO>> getPsychologistsDirectory() {
        return new ResponseEntity<>(usuarioService.getPsychologistsDirectory(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listAll() {
        return new ResponseEntity<>(usuarioService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        return new ResponseEntity<>(usuarioService.update(id, usuarioDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // US13: ConfiguraciÃ³n de Perfil
    @PutMapping("/{id}/profile")
    public ResponseEntity<UsuarioDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO updateDTO) {

        UsuarioDTO usuarioActualizado = usuarioService.updateProfile(id, updateDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<UsuarioDTO> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return ResponseEntity.ok(usuarioService.uploadProfilePicture(id, file));
    }

    @DeleteMapping("/{id}/profile-picture")
    public ResponseEntity<UsuarioDTO> deleteProfilePicture(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.deleteProfilePicture(id));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody com.upc.staycool.DTOs.UsuarioPasswordDTO passwordDTO) {
        usuarioService.changePassword(id, passwordDTO);
        return ResponseEntity.ok().build();
    }
}