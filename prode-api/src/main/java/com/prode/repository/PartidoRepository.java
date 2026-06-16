package com.prode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Partido;

public interface PartidoRepository extends JpaRepository<Partido, Long> {

    List<Partido> findByFechaIdOrderByFechaHoraAsc(Long fechaId);

}