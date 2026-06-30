

package com.prode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.EquipoRequest;
import com.prode.entity.Equipo;
import com.prode.entity.Grupo;
import com.prode.repository.EquipoRepository;
import com.prode.repository.GrupoRepository;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final GrupoRepository grupoRepository;

    public EquipoService(
            EquipoRepository equipoRepository,
            GrupoRepository grupoRepository) {

        this.equipoRepository = equipoRepository;
        this.grupoRepository = grupoRepository;
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

        if (equipoRepository.existsByCodigoFifa(request.getCodigoFifa())) {
            throw new RuntimeException("Ese código FIFA ya existe");
        }

        Grupo grupo = grupoRepository.findById(request.getGrupoId())
                .orElseThrow(() ->
                        new RuntimeException("Grupo no encontrado"));

        Equipo equipo = new Equipo();

        equipo.setNombre(request.getNombre());
        equipo.setCodigoFifa(request.getCodigoFifa());
        equipo.setBandera(request.getBandera());
        equipo.setGrupo(grupo);

        return equipoRepository.save(equipo);
    }

    public Equipo editar(Long id, EquipoRequest request) {

        Equipo equipo = buscarPorId(id);

        Grupo grupo = grupoRepository.findById(request.getGrupoId())
                .orElseThrow(() ->
                        new RuntimeException("Grupo no encontrado"));

        equipo.setNombre(request.getNombre());
        equipo.setCodigoFifa(request.getCodigoFifa());
        equipo.setBandera(request.getBandera());
        equipo.setGrupo(grupo);

        return equipoRepository.save(equipo);
    }

    public void eliminar(Long id) {

        Equipo equipo = buscarPorId(id);

        equipoRepository.delete(equipo);
    }

    public List<Equipo> listarPorGrupo(Long grupoId){
        return equipoRepository.findByGrupoId(grupoId);
    }
}
