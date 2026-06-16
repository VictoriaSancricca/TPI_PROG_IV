package com.prode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Fecha;
import com.prode.enums.EstadoFecha;

public interface FechaRepository extends JpaRepository<Fecha, Long> {

    List<Fecha> findByEstado(EstadoFecha estado);

}
