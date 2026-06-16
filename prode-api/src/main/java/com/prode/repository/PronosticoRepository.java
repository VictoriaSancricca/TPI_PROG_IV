package com.prode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Pronostico;

public interface PronosticoRepository extends JpaRepository<Pronostico, Long> {

    Optional<Pronostico> findByUsuarioIdAndPartidoId(
            Long usuarioId,
            Long partidoId
    );

    List<Pronostico> findByUsuarioId(Long usuarioId);

    List<Pronostico> findByPartidoId(Long partidoId);

}
