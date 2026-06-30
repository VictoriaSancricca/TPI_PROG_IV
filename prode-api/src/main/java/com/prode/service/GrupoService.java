package com.prode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.GrupoRequest;
import com.prode.entity.Grupo;
import com.prode.repository.EquipoRepository;
import com.prode.repository.GrupoRepository;
import java.util.ArrayList;

import com.prode.dto.EquipoResumenDTO;
import com.prode.dto.GrupoResponse;
import com.prode.entity.Equipo; 
import com.prode.dto.TablaPosicionesDTO;
import com.prode.entity.Partido;
import com.prode.enums.EstadoPartido;
import com.prode.repository.PartidoRepository;
import com.prode.entity.Equipo;
import com.prode.repository.EquipoRepository;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final PartidoRepository partidoRepository;
    private final EquipoRepository equipoRepository;

    public GrupoService(
            GrupoRepository grupoRepository,
            EquipoRepository equipoRepository,
            PartidoRepository partidoRepository) {

        this.grupoRepository = grupoRepository;
        this.equipoRepository = equipoRepository;
        this.partidoRepository = partidoRepository;
    }

    public List<GrupoResponse> listar() {

        List<Grupo> grupos = grupoRepository.findAll();

        List<GrupoResponse> respuesta = new ArrayList<>();

        for (Grupo grupo : grupos) {

            GrupoResponse dto = new GrupoResponse();

            dto.setId(grupo.getId());

            dto.setNombre(grupo.getNombre());

            List<EquipoResumenDTO> equiposDTO = new ArrayList<>();

            for (Equipo equipo : grupo.getEquipos()) {

                EquipoResumenDTO e = new EquipoResumenDTO();

                e.setId(equipo.getId());
                e.setNombre(equipo.getNombre());
                e.setCodigoFifa(equipo.getCodigoFifa());
                e.setBandera(equipo.getBandera());

                equiposDTO.add(e);

            }

            dto.setEquipos(equiposDTO);

            respuesta.add(dto);

        }

        return respuesta;

    }

    public Grupo buscarPorId(Long id) {

        return grupoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Grupo no encontrado"));
    }

    public Grupo guardar(GrupoRequest request) {

        if (grupoRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe un grupo con ese nombre");
        }

        Grupo grupo = new Grupo();

        grupo.setNombre(request.getNombre().toUpperCase());

        return grupoRepository.save(grupo);
    }

    public Grupo editar(Long id, GrupoRequest request) {

        Grupo grupo = buscarPorId(id);

        grupo.setNombre(request.getNombre().toUpperCase());

        return grupoRepository.save(grupo);
    }

    public void eliminar(Long id) {

        Grupo grupo = buscarPorId(id);

        grupoRepository.delete(grupo);
    }

    public List<TablaPosicionesDTO> calcularTablaGrupo(Long grupoId) {

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() ->
                        new RuntimeException("Grupo no encontrado"));

        List<Equipo> equipos = grupo.getEquipos();

        List<Partido> partidos =
                partidoRepository.findByGrupoIdAndEstado(
                        grupoId,
                        EstadoPartido.FINALIZADO);

        List<TablaPosicionesDTO> tabla = new ArrayList<>();

        for (Equipo equipo : equipos) {

            TablaPosicionesDTO dto = new TablaPosicionesDTO();

            dto.setIdEquipo(equipo.getId());
            dto.setNombre(equipo.getNombre());
            dto.setBandera(equipo.getBandera());
            dto.setCodigoFifa(equipo.getCodigoFifa());

            tabla.add(dto);

        }

        for (Partido partido : partidos) {

            TablaPosicionesDTO local =
                    buscarEquipoTabla(tabla,
                            partido.getLocal().getId());

            TablaPosicionesDTO visitante =
                    buscarEquipoTabla(tabla,
                            partido.getVisitante().getId());

            local.setPj(local.getPj() + 1);
            visitante.setPj(visitante.getPj() + 1);

            local.setGf(local.getGf() + partido.getGolesLocal());
            local.setGc(local.getGc() + partido.getGolesVisitante());

            visitante.setGf(visitante.getGf() + partido.getGolesVisitante());
            visitante.setGc(visitante.getGc() + partido.getGolesLocal());

            if (partido.getGolesLocal() > partido.getGolesVisitante()) {

                local.setPg(local.getPg() + 1);
                local.setPuntos(local.getPuntos() + 3);

                visitante.setPp(visitante.getPp() + 1);

            } else if (partido.getGolesLocal() < partido.getGolesVisitante()) {

                visitante.setPg(visitante.getPg() + 1);
                visitante.setPuntos(visitante.getPuntos() + 3);

                local.setPp(local.getPp() + 1);

            } else {

                local.setPe(local.getPe() + 1);
                visitante.setPe(visitante.getPe() + 1);

                local.setPuntos(local.getPuntos() + 1);
                visitante.setPuntos(visitante.getPuntos() + 1);

            }

        }

        for (TablaPosicionesDTO dto : tabla) {

            dto.setDg(dto.getGf() - dto.getGc());

        }

        ordenarTabla(tabla);

        return tabla;

    }

    private TablaPosicionesDTO buscarEquipoTabla(
        List<TablaPosicionesDTO> tabla,
        Long idEquipo) {

        for (TablaPosicionesDTO dto : tabla) {

            if (dto.getIdEquipo().equals(idEquipo)) {

                return dto;

            }

        }

        throw new RuntimeException("Equipo no encontrado");

    }

    private void ordenarTabla(List<TablaPosicionesDTO> tabla) {

        tabla.sort((a, b) -> {

            if (b.getPuntos() != a.getPuntos()) {

                return b.getPuntos() - a.getPuntos();

            }

            if (b.getDg() != a.getDg()) {

                return b.getDg() - a.getDg();

            }

            return b.getGf() - a.getGf();

        });

    }

    public Equipo buscarEquipo(Long id){

        return equipoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Equipo no encontrado"));

    }


}
