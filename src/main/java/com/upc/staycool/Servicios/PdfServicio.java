package com.upc.staycool.Servicios;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.upc.staycool.DTOs.PatientDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfServicio {

    @Autowired
    private PsicologoServicio psicologoServicio;

    public byte[] generarPdfPaciente(Long psicologoId, Long pacienteId) {

        PatientDetailDTO dto = psicologoServicio.getPatientDetails(psicologoId, pacienteId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("EXPEDIENTE CLÍNICO DEL PACIENTE").setBold().setFontSize(16));
        document.add(new Paragraph("====================================="));
        document.add(new Paragraph("Nombre: " + dto.getNombre()));
        document.add(new Paragraph("Edad: " + dto.getEdad() + " años"));
        document.add(new Paragraph("Fecha de Ingreso: " + dto.getFechaIngreso()));
        document.add(new Paragraph("Estado Actual: " + dto.getEstadoAlerta()));
        
        document.add(new Paragraph("\nNOTAS CLÍNICAS:").setBold().setFontSize(14));
        document.add(new Paragraph(dto.getNotasClinicas() != null && !dto.getNotasClinicas().isEmpty() ? dto.getNotasClinicas() : "Sin observaciones registradas por el psicólogo."));

        document.add(new Paragraph("\nMÉTRICAS Y METAS:").setBold().setFontSize(14));
        document.add(new Paragraph("Meta diaria límite de Redes Sociales: " + (dto.getMetaDiaria() != null ? dto.getMetaDiaria() : 60) + " minutos"));

        document.add(new Paragraph("\nACTIVIDAD RECIENTE DEL PACIENTE EN LA APP:").setBold().setFontSize(14));
        if (dto.getActividadReciente() != null && !dto.getActividadReciente().isEmpty()) {
            for(PatientDetailDTO.ActividadReciente act : dto.getActividadReciente()) {
                document.add(new Paragraph("• " + act.getTitulo() + " (" + act.getFechaHora() + ")"));
                document.add(new Paragraph("  " + act.getDescripcion()));
            }
        } else {
            document.add(new Paragraph("Sin actividad reciente."));
        }

        document.add(new Paragraph("\nHISTORIAL EMOCIONAL (Últimos días):").setBold().setFontSize(14));
        if (dto.getHistorialEmocional() != null && !dto.getHistorialEmocional().isEmpty()) {
            document.add(new Paragraph("Minutos activos registrados por día: " + dto.getHistorialEmocional().toString()));
        } else {
            document.add(new Paragraph("Sin registros."));
        }

        document.close();
        return out.toByteArray();
    }
}
