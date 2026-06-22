package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.dto.EquipoRequest;
import com.prode.entity.Equipo;
import com.prode.service.EquipoService;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public List<Equipo> listar() {
        return equipoService.listar();
    }

    @GetMapping("/{id}")
    public Equipo buscarPorId(@PathVariable Long id) {
        return equipoService.buscarPorId(id);
    }

    @PostMapping
    public Equipo guardar(
            @RequestBody EquipoRequest request) {

        return equipoService.guardar(request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        equipoService.eliminar(id);
    }
}
