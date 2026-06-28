package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.EventoDTO;
import com.upc.staycool.Entidades.EventoType;
import com.upc.staycool.Servicios.EventoServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reminders")
public class RecordatorioControlador {

    @Autowired
    private EventoServicio eventoServicio;

    // US30: Recordatorios personalizados
    @PostMapping
    public ResponseEntity<EventoDTO> crearRecordatorio(
            @Valid @RequestBody EventoDTO dto) {

        dto.setType(EventoType.PERSONAL_REMINDER);

        return new ResponseEntity<>(
                eventoServicio.crear(dto),
                HttpStatus.CREATED
        );
    }
}
