package com.upc.trabajoparcial.Repositorios;

import com.upc.trabajoparcial.Entidades.EventoEntidad;
import org.springframework.data.jpa.Repository.JpaRepository;

public interface EventoRepositorio extends JpaRepository<EventoEntidad, Long> {
}