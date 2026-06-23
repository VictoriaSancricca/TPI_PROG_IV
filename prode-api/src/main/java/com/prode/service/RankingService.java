package com.prode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.entity.Usuario;
import com.prode.repository.UsuarioRepository;

@Service
public class RankingService {

    private final UsuarioRepository usuarioRepository;

    public RankingService(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerRanking() {

        return usuarioRepository
                .findAllByOrderByPuntosDescPlenosDesc();
    }
}