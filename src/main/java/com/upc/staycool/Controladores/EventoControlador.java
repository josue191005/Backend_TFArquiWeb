package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.EventoDTO;
import com.upc.staycool.Servicios.EventoServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar/events")
public class EventoControlador {

    @Autowired
    private EventoServicio eventoServicio;
    
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtener(@PathVariable Long id) {
        return new ResponseEntity<>(eventoServicio.obtener(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eventoServicio.eliminar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // US18: Revisar calendario de actividades
    @GetMapping
    public ResponseEntity<List<EventoDTO>> revisarCalendario() {
        return new ResponseEntity<>(eventoServicio.listar(), HttpStatus.OK);
    }

    // US42: Agendar eventos en el calendario
    @PostMapping
    public ResponseEntity<EventoDTO> agendarEvento(@Valid @RequestBody EventoDTO dto) {
        return new ResponseEntity<>(eventoServicio.crear(dto), HttpStatus.CREATED);
    }

    // US42: Sincronizar evento manual
    @PostMapping("/{id}/sync")
    public ResponseEntity<EventoDTO> sincronizarManual(@PathVariable Long id) {
        return new ResponseEntity<>(eventoServicio.sincronizarConGoogleManual(id), HttpStatus.OK);
    }
}