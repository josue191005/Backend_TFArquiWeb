package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.LogroDTO;
import com.upc.staycool.Servicios.LogroServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achievements")
public class LogroControlador {

    @Autowired
    private LogroServicio logroServicio;

    @PostMapping
    public ResponseEntity<LogroDTO> crear(@Valid @RequestBody LogroDTO dto) {
        return new ResponseEntity<>(logroServicio.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LogroDTO>> listarTodos() {
        return new ResponseEntity<>(logroServicio.listarTodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogroDTO> buscarPorId(@PathVariable Long id) {
        return new ResponseEntity<>(logroServicio.buscarPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogroDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LogroDTO dto) {
        return new ResponseEntity<>(logroServicio.actualizar(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logroServicio.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}