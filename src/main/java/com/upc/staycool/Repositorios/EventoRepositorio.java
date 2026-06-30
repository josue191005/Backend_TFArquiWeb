package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.EventoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepositorio extends JpaRepository<EventoEntidad, Long> {

}