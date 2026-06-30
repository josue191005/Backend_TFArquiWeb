package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.MensajeEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensajeRepositorio extends JpaRepository<MensajeEntidad, UUID> {
    List<MensajeEntidad> findByChat_IdOrderByTimestampAsc(UUID chatId);
}
