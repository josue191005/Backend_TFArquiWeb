package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.AsignacionRecursoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRecursoRepositorio extends JpaRepository<AsignacionRecursoEntidad, Long> {
    List<AsignacionRecursoEntidad> findByPacienteId(Long pacienteId);
    List<AsignacionRecursoEntidad> findByRecursoId(Long recursoId);
    boolean existsByRecursoIdAndPacienteId(Long recursoId, Long pacienteId);
}
