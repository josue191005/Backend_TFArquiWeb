package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.RecursoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepositorio extends JpaRepository<RecursoEntidad, Long> {

}