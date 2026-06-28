package com.upc.staycool.Controladores;

import com.upc.staycool.DTOs.AlertaDTO;
import com.upc.staycool.DTOs.PsicologoDashboardDTO;
import com.upc.staycool.Servicios.PsicologoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.upc.staycool.Servicios.PdfServicio;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/psychologist")
public class PsicologoControlador {
    @Autowired
    private PsicologoServicio servicio;

    @Autowired
    private PdfServicio pdfServicio;

    @PostMapping("/assign-patient")
    public org.springframework.http.ResponseEntity<String> assignPatient(
            @RequestParam Long psicologoId, 
            @RequestParam Long pacienteId,
            @RequestBody(required = false) java.util.Map<String, String> body) {
        try {
            String mensaje = body != null ? body.get("mensajeSolicitud") : null;
            servicio.asignarPaciente(psicologoId, pacienteId, mensaje != null ? mensaje : "Me gustaría conectar contigo.");
            return org.springframework.http.ResponseEntity.ok("Solicitud enviada exitosamente");
        } catch (RuntimeException e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test-requests")
    public List<com.upc.staycool.DTOs.SolicitudDTO> testSolicitudes(@RequestParam Long psicologoId) {
        return servicio.obtenerSolicitudesPendientes(psicologoId);
    }

    @GetMapping("/requests")
    public List<com.upc.staycool.DTOs.SolicitudDTO> obtenerSolicitudes(@RequestParam Long psicologoId) {
        System.out.println("=== API /requests called for psicologoId: " + psicologoId);
        List<com.upc.staycool.DTOs.SolicitudDTO> res = servicio.obtenerSolicitudesPendientes(psicologoId);
        System.out.println("=== API /requests returning size: " + res.size());
        return res;
    }

    @PutMapping("/requests/{solicitudId}/accept")
    public String aceptarSolicitud(@PathVariable Long solicitudId) {
        servicio.aceptarSolicitud(solicitudId);
        return "Solicitud aceptada";
    }

    @PutMapping("/requests/{solicitudId}/reject")
    public String rechazarSolicitud(@PathVariable Long solicitudId) {
        servicio.rechazarSolicitud(solicitudId);
        return "Solicitud rechazada";
    }

    // US32: Panel de indicadores clínicos
    @GetMapping("/dashboard/metrics")
    public PsicologoDashboardDTO obtenerMetricasDashboard(@RequestParam Long psicologoId) {
        return servicio.obtenerMetricasDashboard(psicologoId);
    }

    // US10: GET /api/v1/psychologist/patients/monitoring
    @GetMapping("/patients/monitoring")
    public java.util.List<com.upc.staycool.DTOs.PatientMonitoreoDTO> monitorearPacientes(@RequestParam Long psicologoId) {
        return servicio.monitorearPacientes(psicologoId);
    }

    @GetMapping("/alerts/critical")
    public List<AlertaDTO> listarAlertasCriticasHistoria(@RequestParam Long psychologistId) {
        return servicio.getCriticalAlerts(psychologistId);
    }

    @GetMapping("/patients/{patientId}/details")
    public com.upc.staycool.DTOs.PatientDetailDTO getPatientDetails(
            @RequestParam Long psicologoId,
            @PathVariable Long patientId) {
        return servicio.getPatientDetails(psicologoId, patientId);
    }

    @PostMapping("/patients/{patientId}/notes")
    public String savePatientNotes(
            @RequestParam Long psicologoId,
            @PathVariable Long patientId,
            @RequestBody java.util.Map<String, String> body) {
        String notes = body.get("notes");
        servicio.savePatientNotes(psicologoId, patientId, notes);
        return "Notas actualizadas";
    }

    // US12: Exportar información del paciente
    @PostMapping("/patients/{patientId}/export/pdf")
    public ResponseEntity<byte[]> exportarPdfPacientes(
            @RequestParam Long psicologoId,
            @PathVariable Long patientId) {

        byte[] pdf = pdfServicio.generarPdfPaciente(psicologoId, patientId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=paciente_" + patientId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
