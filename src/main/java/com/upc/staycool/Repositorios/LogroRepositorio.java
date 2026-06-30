package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.LogroEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogroRepositorio extends JpaRepository<LogroEntidad, Long> {
}