package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.dto.UsuarioResponse;
import com.prode.entity.Usuario;
import com.prode.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listarUsuarios() {

        return usuarioService.listarTodos()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/ranking")
    public List<UsuarioResponse> ranking() {

        return usuarioService.ranking()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(
            @PathVariable Long id) {

        return toResponse(
                usuarioService.buscarPorId(id));
    }

    private UsuarioResponse toResponse(
            Usuario usuario) {

        return new UsuarioResponse(

                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPuntos(),
                usuario.getPlenos()
        );
    }

}
