package com.upc.trabajoparcial.Repositorios;

import com.upc.trabajoparcial.Entidades.ChatEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepositorio extends JpaRepository<ChatEntidad, UUID> {
}
