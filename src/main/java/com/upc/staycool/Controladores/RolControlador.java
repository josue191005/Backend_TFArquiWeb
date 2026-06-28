package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.RolDTO;
import com.upc.staycool.Servicios.RolServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolControlador {

    @Autowired
    private RolServicio rolService;

    @PostMapping
    public ResponseEntity<RolDTO> create(@Valid @RequestBody RolDTO rolDTO) {
        return new ResponseEntity<>(rolService.create(rolDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RolDTO>> listAll() {
        return new ResponseEntity<>(rolService.listAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> update(@PathVariable Long id, @Valid @RequestBody RolDTO rolDTO) {
        return new ResponseEntity<>(rolService.update(id, rolDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}