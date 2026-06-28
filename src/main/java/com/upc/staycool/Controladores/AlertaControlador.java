package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.AlertaDTO;
import com.upc.staycool.Servicios.AlertaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertaControlador {

    @Autowired
    private AlertaServicio alertaServicio;

    @PostMapping
    public ResponseEntity<AlertaDTO> crear(@RequestBody AlertaDTO dto) {
        return new ResponseEntity<>(alertaServicio.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AlertaDTO>> listarTodas() {
        return new ResponseEntity<>(alertaServicio.listarTodas(), HttpStatus.OK);
    }

    @GetMapping("/user/{receptorId}")
    public ResponseEntity<List<AlertaDTO>> listarPorReceptor(@PathVariable Long receptorId) {
        return new ResponseEntity<>(alertaServicio.listarPorReceptor(receptorId), HttpStatus.OK);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        alertaServicio.marcarComoLeida(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // US33: NotificaciÃ³n de urgencia
    @PostMapping("/urgent/trigger")
    public ResponseEntity<AlertaDTO> alertaUrgente(@RequestBody AlertaDTO dto) {
        // Por si no envÃ­an el tipo, forzamos que sea URGENT
        if (dto.getTipo() == null && dto.getAlertLevel() != null) {
            dto.setTipo(dto.getAlertLevel());
        } else if (dto.getTipo() == null) {
            dto.setTipo("URGENT");
        }
        return new ResponseEntity<>(alertaServicio.crear(dto), HttpStatus.CREATED);
    }
}