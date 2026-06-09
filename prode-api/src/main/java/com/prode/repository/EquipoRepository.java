package com.prode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Equipo;

public interface EquipoRepository
        extends JpaRepository<Equipo, Long> {
}