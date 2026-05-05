package com.upc.trabajoparcial.Controladores;

import com.upc.trabajoparcial.DTOs.ChatDTO;
import com.upc.trabajoparcial.Servicios.ChatServicio;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatControlador {

    @Autowired
    private ChatServicio service;

    @PostMapping("/crear")
    public ChatDTO crear(@RequestBody ChatDTO dto) {
        return service.crear(dto);
    }

    @GetMapping("/listar-todos")
    public List<ChatDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/obtener/{id}")
    public ChatDTO obtener(@PathVariable UUID id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/actualizar/{id}")
    public ChatDTO actualizar(@PathVariable UUID id, @RequestBody ChatDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable UUID id) {
        service.eliminar(id);
    }

    // --- CASCARÓN: US25 REVISAR HISTORIAL DE CHATS ---
    @GetMapping("/historial")
    public String obtenerHistorialChats() {
        return "Cascarón: Aquí se devolverá la lista de conversaciones anteriores (US25)";
    }
}
