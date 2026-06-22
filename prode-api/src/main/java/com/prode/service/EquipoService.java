package com.prode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.EquipoRequest;
import com.prode.entity.Equipo;
import com.prode.repository.EquipoRepository;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> listar() {
        return equipoRepository.findAll();
    }

    public Equipo buscarPorId(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Equipo no encontrado"));
    }

    public Equipo guardar(EquipoRequest request) {

        if (equipoRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe un equipo con ese nombre");
        }

        Equipo equipo = new Equipo();

        equipo.setNombre(request.getNombre());

        return equipoRepository.save(equipo);
    }

    public void eliminar(Long id) {

        Equipo equipo = buscarPorId(id);

        equipoRepository.delete(equipo);
    }
}
