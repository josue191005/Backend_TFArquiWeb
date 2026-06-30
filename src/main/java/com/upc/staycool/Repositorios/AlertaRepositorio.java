package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.AlertaEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepositorio extends JpaRepository<AlertaEntidad, Long> {
    @Query("SELECT COUNT(a) FROM AlertaEntidad a WHERE a.receptor.id = :receptorId AND a.leido = false")
    Long countAlertasNoLeidasPorReceptor(@Param("receptorId") Long receptorId);

    List<AlertaEntidad> findByReceptor_IdOrderByFechaCreacionDesc(Long receptorId);
}