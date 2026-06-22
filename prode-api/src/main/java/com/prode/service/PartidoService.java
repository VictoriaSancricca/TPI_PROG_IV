package com.prode.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.PartidoRequest;
import com.prode.entity.Equipo;
import com.prode.entity.Fecha;
import com.prode.entity.Partido;
import com.prode.enums.EstadoPartido;
import com.prode.repository.EquipoRepository;
import com.prode.repository.FechaRepository;
import com.prode.repository.PartidoRepository;

@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final FechaRepository fechaRepository;
    private final EquipoRepository equipoRepository;

    public PartidoService(
            PartidoRepository partidoRepository,
            FechaRepository fechaRepository,
            EquipoRepository equipoRepository) {

        this.partidoRepository = partidoRepository;
        this.fechaRepository = fechaRepository;
        this.equipoRepository = equipoRepository;
    }

    public List<Partido> listar() {
        return partidoRepository.findAll();
    }

    public Partido buscarPorId(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Partido no encontrado"));
    }

    public List<Partido> listarPorFecha(Long fechaId) {
        return partidoRepository.findByFechaIdOrderByFechaHoraAsc(fechaId);
    }

    public Partido guardar(PartidoRequest request) {

        if (request.getLocalId().equals(request.getVisitanteId())) {
            throw new RuntimeException(
                    "Local y visitante no pueden ser el mismo equipo");
        }

        Fecha fecha = fechaRepository.findById(request.getFechaId())
                .orElseThrow(() ->
                        new RuntimeException("Fecha no encontrada"));

        Equipo local = equipoRepository.findById(request.getLocalId())
                .orElseThrow(() ->
                        new RuntimeException("Equipo local no encontrado"));

        Equipo visitante = equipoRepository.findById(request.getVisitanteId())
                .orElseThrow(() ->
                        new RuntimeException("Equipo visitante no encontrado"));

        Partido partido = new Partido();

        partido.setFecha(fecha);
        partido.setLocal(local);
        partido.setVisitante(visitante);
        partido.setFechaHora(request.getFechaHora());

        partido.setEstado(EstadoPartido.POR_JUGARSE);

        return partidoRepository.save(partido);
    }

    public void eliminar(Long id) {

        Partido partido = buscarPorId(id);

        if (partido.getEstado() != EstadoPartido.POR_JUGARSE) {
            throw new RuntimeException(
                    "Solo se pueden eliminar partidos por jugarse");
        }

        partidoRepository.delete(partido);
    }
}
