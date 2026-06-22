package com.prode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.FechaRequest;
import com.prode.entity.Fecha;
import com.prode.enums.EstadoFecha;
import com.prode.repository.FechaRepository;

@Service
public class FechaService {

    private final FechaRepository fechaRepository;

    public FechaService(FechaRepository fechaRepository) {
        this.fechaRepository = fechaRepository;
    }

    public List<Fecha> listar() {
        return fechaRepository.findAll();
    }

    public Fecha buscarPorId(Long id) {
        return fechaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Fecha no encontrada"));
    }

    public Fecha guardar(FechaRequest request) {

        Fecha fecha = new Fecha();

        fecha.setNombre(request.getNombre());

        fecha.setEstado(EstadoFecha.PROGRAMADA);

        return fechaRepository.save(fecha);
    }

    public Fecha actualizar(Long id, FechaRequest request) {

        Fecha fecha = buscarPorId(id);

        fecha.setNombre(request.getNombre());

        return fechaRepository.save(fecha);
    }

    public void eliminar(Long id) {

        Fecha fecha = buscarPorId(id);

        fechaRepository.delete(fecha);
    }
}
