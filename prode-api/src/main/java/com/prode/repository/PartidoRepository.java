package com.prode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Partido;
import com.prode.enums.EstadoPartido;
import com.prode.enums.FaseMundial;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

    List<Partido> findByGrupoIdAndEstado(
            Long grupoId,
            EstadoPartido estado);

    List<Partido> findByEstadoOrderByFechaHoraAsc(
            EstadoPartido estado);

    List<Partido> findByFase(FaseMundial fase);

    List<Partido> findByFaseAndEstado(
            FaseMundial fase,
            EstadoPartido estado);

    
}