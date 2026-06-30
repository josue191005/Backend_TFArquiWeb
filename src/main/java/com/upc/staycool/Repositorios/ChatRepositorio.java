package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.ChatEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepositorio extends JpaRepository<ChatEntidad, UUID> {
    List<ChatEntidad> findByPatient_Id(Long patientId);
    List<ChatEntidad> findByPsychologist_Id(Long psychologistId);
}
