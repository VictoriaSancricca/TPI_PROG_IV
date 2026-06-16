package com.prode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

}
