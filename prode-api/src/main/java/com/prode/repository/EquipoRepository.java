package com.prode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Equipo;
import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    boolean existsByNombre(String nombre);
    boolean existsByCodigoFifa(String codigoFifa);
    List<Equipo> findByGrupoId(Long grupoId);
}
