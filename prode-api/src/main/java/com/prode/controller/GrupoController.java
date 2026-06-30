package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.service.GrupoService;
import com.prode.dto.GrupoRequest;
import com.prode.dto.GrupoResponse;
import com.prode.dto.TablaPosicionesDTO;
import com.prode.entity.Grupo;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<GrupoResponse> listar() {
        return grupoService.listar();
    }

    @GetMapping("/{id}")
    public Grupo buscarPorId(@PathVariable Long id) {
        return grupoService.buscarPorId(id);
    }

    @PostMapping
    public Grupo guardar(@RequestBody GrupoRequest request) {
        return grupoService.guardar(request);
    }

    @PutMapping("/{id}")
    public Grupo editar(
            @PathVariable Long id,
            @RequestBody GrupoRequest request) {

        return grupoService.editar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        grupoService.eliminar(id);
    }

    @GetMapping("/{id}/tabla")
    public List<TablaPosicionesDTO> tabla(@PathVariable Long id) {

        return grupoService.calcularTablaGrupo(id);

    }

}
