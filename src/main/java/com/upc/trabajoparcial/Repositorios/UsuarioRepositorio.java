package com.upc.trabajoparcial.Repositorios;

import com.upc.trabajoparcial.Entidades.UsuarioEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<UsuarioEntidad, Long> {

    // Le enseñamos a Spring Boot a buscar un usuario por su columna "email"
    Optional<UsuarioEntidad> findByEmail(String email);
}