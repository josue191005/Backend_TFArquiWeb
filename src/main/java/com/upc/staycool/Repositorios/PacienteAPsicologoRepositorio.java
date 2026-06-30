package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.PacienteAPsicologoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteAPsicologoRepositorio extends JpaRepository<PacienteAPsicologoEntidad, Long>{
    
    List<PacienteAPsicologoEntidad> findByPsicologoId(Long psicologoId);
    Optional<PacienteAPsicologoEntidad> findFirstByPacienteId(Long pacienteId);
    Optional<PacienteAPsicologoEntidad> findByPsicologoIdAndPacienteId(Long psicologoId, Long pacienteId);
    boolean existsByPsicologoIdAndPacienteId(Long psicologoId, Long pacienteId);
    
    // Para listar solicitudes entrantes o revisar si ya hay una solicitud
    List<PacienteAPsicologoEntidad> findByPsicologoIdAndStatus(Long psicologoId, String status);
    Optional<PacienteAPsicologoEntidad> findByPsicologoIdAndPacienteIdAndStatus(Long psicologoId, Long pacienteId, String status);
    
    // Cuenta cuántos pacientes tiene asignado este psicólogo (solo aceptados)
    @Query("SELECT COUNT(p) FROM PacienteAPsicologoEntidad p WHERE p.psicologo.id = :psicologoId AND p.status = 'ACCEPTED'")
    Long countPacientesByPsicologoId(@Param("psicologoId") Long psicologoId);

    // US10: Traer toda la lista de pacientes de un psicólogo (solo aceptados)
    @Query("SELECT p FROM PacienteAPsicologoEntidad p WHERE p.psicologo.id = :psicologoId AND p.status = 'ACCEPTED'")
    List<PacienteAPsicologoEntidad> findPacientesByPsicologoId(@Param("psicologoId") Long psicologoId);
}
