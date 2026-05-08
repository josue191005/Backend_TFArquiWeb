package com.upc.trabajoparcial.Repositorios;

import com.upc.trabajoparcial.Entidades.EventoEntidad;
// ¡Corregido! 'repository' va en minúscula
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepositorio extends JpaRepository<EventoEntidad, Long> {

}