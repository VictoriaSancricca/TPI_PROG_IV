package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.dto.PronosticoRequest;
import com.prode.entity.Pronostico;
import com.prode.service.PronosticoService;

@RestController
@RequestMapping("/pronosticos")
public class PronosticoController {

    private final PronosticoService pronosticoService;

    public PronosticoController(
            PronosticoService pronosticoService) {

        this.pronosticoService = pronosticoService;
    }

    @GetMapping
    public List<Pronostico> listar() {
        return pronosticoService.listar();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pronostico> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return pronosticoService
                .listarPorUsuario(usuarioId);
    }

    @GetMapping("/partido/{partidoId}")
    public List<Pronostico> listarPorPartido(
            @PathVariable Long partidoId) {

        return pronosticoService
                .listarPorPartido(partidoId);
    }

    @PostMapping
    public Pronostico guardar(
            @RequestBody PronosticoRequest request) {

        return pronosticoService.guardar(request);
    }
}
