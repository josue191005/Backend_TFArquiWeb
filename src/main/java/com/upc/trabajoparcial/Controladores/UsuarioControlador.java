package com.upc.trabajoparcial.Controladores;

import com.upc.trabajoparcial.DTOs.UsuarioDTO;
import com.upc.trabajoparcial.Repositorios.UsuarioRepositorio;
import com.upc.trabajoparcial.Servicios.UsuarioServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upc.trabajoparcial.DTOs.UsuarioUpdateDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return new ResponseEntity<>(usuarioService.create(usuarioDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listAll() {
        return new ResponseEntity<>(usuarioService.listAll(), HttpStatus.OK);
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

    // US13: Configuración de Perfil
    @PutMapping("/{id}/profile")
    public ResponseEntity<UsuarioDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO updateDTO) {

        UsuarioDTO usuarioActualizado = usuarioService.updateProfile(id, updateDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }
}