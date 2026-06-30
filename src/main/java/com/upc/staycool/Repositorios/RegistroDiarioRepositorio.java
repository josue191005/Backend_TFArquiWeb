package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.RegistroDiarioEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroDiarioRepositorio extends JpaRepository<RegistroDiarioEntidad, Long>{

    // US05: La BD suma automÃ¡ticamente todos los minutos activos de un paciente
    @Query("SELECT COALESCE(SUM(r.minutosActivos), 0) FROM RegistroDiarioEntidad r WHERE r.usuario.id = :usuarioId")
    Integer sumarMinutosActivosPorUsuario(@Param("usuarioId") Long usuarioId);

    // US05: La BD suma automÃ¡ticamente todos los minutos de descanso
    @Query("SELECT COALESCE(SUM(r.minutosDescanso), 0) FROM RegistroDiarioEntidad r WHERE r.usuario.id = :usuarioId")
    Integer sumarMinutosDescansoPorUsuario(@Param("usuarioId") Long usuarioId);

    boolean existsByUsuarioIdAndFechaRegistro(Long usuarioId, LocalDate fechaRegistro);
    Optional<RegistroDiarioEntidad> findFirstByUsuarioIdAndFechaRegistroOrderByIdDesc(Long usuarioId, LocalDate fechaRegistro);
    List<RegistroDiarioEntidad> findByUsuarioIdOrderByFechaRegistroDesc(Long usuarioId);
    List<RegistroDiarioEntidad> findTop7ByUsuarioIdOrderByFechaRegistroDesc(Long usuarioId);
}
