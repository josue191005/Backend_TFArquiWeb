package com.upc.trabajoparcial.Controladores;

import com.upc.trabajoparcial.DTOs.MensajeDTO;
import com.upc.trabajoparcial.Servicios.MensajeServicio;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mensajes")
public class MensajeControlador {

    @Autowired
    private MensajeServicio service;

    @PostMapping("/crear")
    public MensajeDTO crear(@RequestBody MensajeDTO dto) {
        return service.crear(dto);
    }

    @GetMapping("/listar-todos")
    public List<MensajeDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/obtener/{id}")
    public MensajeDTO obtener(@PathVariable UUID id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/chat/{chatId}")
    public List<MensajeDTO> listarPorChat(@PathVariable UUID chatId) {
        return service.listarPorChat(chatId);
    }

    @PutMapping("/actualizar/{id}")
    public MensajeDTO actualizar(@PathVariable UUID id, @RequestBody MensajeDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable UUID id) {
        service.eliminar(id);
    }
}
