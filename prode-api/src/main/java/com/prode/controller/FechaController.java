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

import com.prode.dto.FechaRequest;
import com.prode.entity.Fecha;
import com.prode.service.FechaService;

@RestController
@RequestMapping("/fechas")
public class FechaController {

    private final FechaService fechaService;

    public FechaController(FechaService fechaService) {
        this.fechaService = fechaService;
    }

    @GetMapping
    public List<Fecha> listar(){
        return fechaService.listar();
    }

    @GetMapping("/{id}")
    public Fecha buscarPorId(@PathVariable Long id) {
        return fechaService.buscarPorId(id);
    }

    @PostMapping
    public Fecha guardar(
            @RequestBody FechaRequest request) {

        return fechaService.guardar(request);
    }

    @PutMapping("/{id}")
    public Fecha actualizar(
            @PathVariable Long id,
            @RequestBody FechaRequest request) {

        return fechaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        fechaService.eliminar(id);
    }

   
}
