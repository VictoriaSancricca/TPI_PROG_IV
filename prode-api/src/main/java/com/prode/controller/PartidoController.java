package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.dto.PartidoRequest;
import com.prode.entity.Partido;
import com.prode.service.PartidoService;

@RestController
@RequestMapping("/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public List<Partido> listar() {
        return partidoService.listar();
    }

    @GetMapping("/{id}")
    public Partido buscarPorId(@PathVariable Long id) {
        return partidoService.buscarPorId(id);
    }

    @GetMapping("/fecha/{fechaId}")
    public List<Partido> listarPorFecha(
            @PathVariable Long fechaId) {

        return partidoService.listarPorFecha(fechaId);
    }

    @PostMapping
    public Partido guardar(
            @RequestBody PartidoRequest request) {

        return partidoService.guardar(request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        partidoService.eliminar(id);
    }
}
