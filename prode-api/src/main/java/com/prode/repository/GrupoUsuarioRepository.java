package com.prode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prode.entity.GrupoUsuario;

public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Long> {

    List<GrupoUsuario> findByGrupoId(Long grupoId);

    List<GrupoUsuario> findByUsuarioId(Long usuarioId);

}
