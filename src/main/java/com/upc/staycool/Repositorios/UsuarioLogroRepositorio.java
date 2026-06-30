package com.upc.staycool.Repositorios;

import com.upc.staycool.Entidades.UsuarioLogroEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioLogroRepositorio extends JpaRepository<UsuarioLogroEntidad, Long> {
      List<UsuarioLogroEntidad> findByUsuarioId(Long usuarioId);
}
