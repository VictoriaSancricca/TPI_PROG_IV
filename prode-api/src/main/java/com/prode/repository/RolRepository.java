package com.prode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Rol;
import com.prode.enums.RolNombre;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(RolNombre nombre);

}
