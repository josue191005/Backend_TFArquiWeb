package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.ChatDTO;
import com.upc.staycool.DTOs.MensajeDTO;
import com.upc.staycool.Servicios.ChatServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // AquÃ­ ya arreglamos el import que estaba mal
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatControlador {

    @Autowired
    private ChatServicio service;

    @PostMapping("/create")
    public ChatDTO crear(@RequestBody ChatDTO dto) {
        return service.crear(dto);
    }

    @GetMapping("/all")
    public List<ChatDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/my-chats")
    public List<ChatDTO> misChats(@RequestParam Long userId, @RequestParam String role) {
        return service.listarMisChats(userId, role);
    }

    @GetMapping("/{id}")
    public ChatDTO obtener(@PathVariable UUID id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public ChatDTO actualizar(@PathVariable UUID id, @RequestBody ChatDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable UUID id) {
        service.eliminar(id);
    }

    // US25: Revisar historial de chats
    @GetMapping("/history")
    public List<MensajeDTO> obtenerHistorial(@RequestParam UUID chatId) {
        return service.obtenerHistorial(chatId);
    }

    // US07: Enviar Mensaje por WebSockets
    @PostMapping("/messages")
    public MensajeDTO enviarMensaje(@RequestBody MensajeDTO mensajeDTO) {
        return service.guardarYEnviarMensaje(mensajeDTO);
    }

    @PostMapping("/emergency/{patientId}")
    public ChatDTO solicitarChatEmergencia(@PathVariable Long patientId) {
        return service.solicitarChatEmergencia(patientId);
    }
}
