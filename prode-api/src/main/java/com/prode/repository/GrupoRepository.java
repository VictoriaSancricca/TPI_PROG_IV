package com.prode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    Optional<Grupo> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}
