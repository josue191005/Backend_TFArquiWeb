package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.RecursoDTO;
import com.upc.staycool.Servicios.RecursoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
public class RecursoControlador {

    @Autowired
    private RecursoServicio recursoServicio;

    @PostMapping
    public ResponseEntity<RecursoDTO> crear(@RequestBody RecursoDTO dto) {
        return new ResponseEntity<>(recursoServicio.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listarTodos() {
        return new ResponseEntity<>(recursoServicio.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoDTO> obtener(@PathVariable Long id) {
        return new ResponseEntity<>(recursoServicio.obtener(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recursoServicio.eliminar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // US08: Buscar informaciÃ³n para problemas especÃ­ficos
    @GetMapping("/search")
    public ResponseEntity<List<RecursoDTO>> buscarInformacion() {
        return new ResponseEntity<>(recursoServicio.listar(), HttpStatus.OK);
    }

    // US09: Realizar ejercicoos de relajaciÃ³n
    @GetMapping("/relaxation")
    public ResponseEntity<List<RecursoDTO>> ejerciciosRelajacion() {
        return new ResponseEntity<>(recursoServicio.listarRecursosRelajacion(), HttpStatus.OK);
    }

    // US11: Subir materiales a la nube
    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecursoDTO> subirMaterial(
            @RequestPart("file") org.springframework.web.multipart.MultipartFile file,
            @RequestPart("recurso") RecursoDTO dto) {

        return new ResponseEntity<>(recursoServicio.subirArchivoYCrear(file, dto), HttpStatus.CREATED);
    }

    @GetMapping("/{recursoId}/assigned-patients")
    public ResponseEntity<List<Long>> getAssignedPatients(@PathVariable Long recursoId) {
        return ResponseEntity.ok(recursoServicio.getAssignedPatientIds(recursoId));
    }

    @PostMapping("/{recursoId}/assign")
    public ResponseEntity<Void> assignResource(
            @PathVariable Long recursoId,
            @RequestParam Long psicologoId,
            @RequestBody List<Long> patientIds) {
        recursoServicio.assignResourceToPatients(recursoId, psicologoId, patientIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/assigned/{patientId}")
    public ResponseEntity<List<RecursoDTO>> getAssignedResources(@PathVariable Long patientId) {
        return new ResponseEntity<>(recursoServicio.getAssignedResourcesForPatient(patientId), HttpStatus.OK);
    }
}