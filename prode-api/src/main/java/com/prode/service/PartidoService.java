package com.prode.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.PartidoRequest;
import com.prode.dto.ResultadoRequest;
import com.prode.entity.Equipo;
import com.prode.entity.Fecha;
import com.prode.entity.Partido;
import com.prode.entity.Pronostico;
import com.prode.entity.Usuario;
import com.prode.enums.EstadoPartido;
import com.prode.repository.EquipoRepository;
import com.prode.repository.FechaRepository;
import com.prode.repository.PartidoRepository;
import com.prode.repository.PronosticoRepository;
import com.prode.repository.UsuarioRepository;
import com.prode.enums.EstadoPartido;
import com.prode.enums.Tendencia;

@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final FechaRepository fechaRepository;
    private final EquipoRepository equipoRepository;
    private final PronosticoRepository pronosticoRepository;
     final UsuarioRepository usuarioRepository;
    
     public PartidoService(
        PartidoRepository partidoRepository,
        FechaRepository fechaRepository,
        EquipoRepository equipoRepository,
        PronosticoRepository pronosticoRepository,
        UsuarioRepository usuarioRepository) {

    this.partidoRepository = partidoRepository;
    this.fechaRepository = fechaRepository;
    this.equipoRepository = equipoRepository;
    this.pronosticoRepository = pronosticoRepository;
    this.usuarioRepository = usuarioRepository;
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

    public Partido registrarResultado(
        Long partidoId,
        ResultadoRequest request) {

        Partido partido = buscarPorId(partidoId);

        partido.setGolesLocal(request.getGolesLocal());
        partido.setGolesVisitante(request.getGolesVisitante());

        Tendencia tendencia = calcularTendencia(
                request.getGolesLocal(),
                request.getGolesVisitante());

        partido.setTendencia(tendencia);

        partido.setEstado(EstadoPartido.FINALIZADO);

        partidoRepository.save(partido);

        procesarPuntajes(partido);

        return partido;
    }

    private Tendencia calcularTendencia(
        Integer golesLocal,
        Integer golesVisitante) {

        if (golesLocal > golesVisitante) {
            return Tendencia.LOCAL;
        }

        if (golesVisitante > golesLocal) {
            return Tendencia.VISITANTE;
        }

        return Tendencia.EMPATE;
    }

    private void procesarPuntajes(Partido partido) {

        ArrayList<Pronostico> pronosticos =
                new ArrayList<>(
                        pronosticoRepository.findByPartidoId(
                                partido.getId()));

        for (Pronostico pronostico : pronosticos) {

            int puntos = calcularPuntos(
                    pronostico,
                    partido);

            pronostico.setPuntos(puntos);

            pronosticoRepository.save(pronostico);

            Usuario usuario = pronostico.getUsuario();

            usuario.setPuntos(
                    usuario.getPuntos() + puntos);

            if (puntos == 3) {

                usuario.setPlenos(
                        usuario.getPlenos() + 1);
            }

            usuarioRepository.save(usuario);
        }
    }

    private int calcularPuntos(
        Pronostico pronostico,
        Partido partido) {

        boolean resultadoExacto =
                pronostico.getGolesLocal()
                        .equals(partido.getGolesLocal())
                &&
                pronostico.getGolesVisitante()
                        .equals(partido.getGolesVisitante());

        if (resultadoExacto) {
            return 3;
        }

        Tendencia tendenciaPronostico =
                calcularTendencia(
                        pronostico.getGolesLocal(),
                        pronostico.getGolesVisitante());

        if (tendenciaPronostico
                == partido.getTendencia()) {

            return 1;
        }

        return 0;
    }
}
