package com.prode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Partido;
import com.prode.enums.EstadoPartido;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

    List<Partido> findByFechaIdOrderByFechaHoraAsc(Long fechaId);

    List<Partido> findByEstadoOrderByFechaHoraAsc(EstadoPartido estado);

    List<Partido> findByGrupoIdOrderByFechaHoraAsc(Long grupoId);

    List<Partido> findByGrupoIdAndEstado(
        Long grupoId,
        EstadoPartido estado);
}