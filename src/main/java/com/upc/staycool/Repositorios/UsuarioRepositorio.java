package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.UsuarioEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<UsuarioEntidad, Long> {

    // Le enseÃ±amos a Spring Boot a buscar un usuario por su columna "email"
    Optional<UsuarioEntidad> findByEmail(String email);

    // Listar por rol
    java.util.List<UsuarioEntidad> findByRolEntidad_Name(String name);
}