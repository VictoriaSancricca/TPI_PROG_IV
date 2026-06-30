package com.prode.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.service.FaseService;

@RestController
@RequestMapping("/fases")
public class FaseController {

    private final FaseService faseService;

    public FaseController(FaseService faseService) {
        this.faseService = faseService;
    }

    @PostMapping("/generar-grupos")
    public void generarPartidosGrupos() {

        faseService.generarPartidosGrupos();

    }

    @PostMapping("/generar")
    public void generarSiguienteFase() {

        faseService.generarSiguienteFase();

    }

}
