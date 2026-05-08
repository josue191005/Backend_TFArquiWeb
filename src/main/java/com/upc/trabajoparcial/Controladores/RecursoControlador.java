package com.upc.trabajoparcial.Controladores;

import com.upc.trabajoparcial.DTOs.RecursoDTO;
import com.upc.trabajoparcial.Servicios.RecursoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
public class RecursoControlador {

    @Autowired
    private RecursoServicio recursoService;

    @PostMapping
    public ResponseEntity<RecursoDTO> crear(@RequestBody RecursoDTO dto) {
        return new ResponseEntity<>(recursoService.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listarTodos() {
        return new ResponseEntity<>(recursoService.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoDTO> obtener(@PathVariable Long id) {
        return new ResponseEntity<>(recursoService.obtener(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recursoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}