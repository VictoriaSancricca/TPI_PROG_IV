package com.prode.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.prode.dto.PartidoRequest;
import com.prode.dto.ResultadoRequest;
import com.prode.entity.Equipo;
import com.prode.entity.Grupo;
import com.prode.entity.Partido;
import com.prode.entity.Pronostico;
import com.prode.entity.Usuario;
import com.prode.enums.EstadoPartido;
import com.prode.enums.FaseMundial;
import com.prode.enums.Tendencia;
import com.prode.repository.EquipoRepository;
import com.prode.repository.GrupoRepository;
import com.prode.repository.PartidoRepository;
import com.prode.repository.PronosticoRepository;
import com.prode.repository.UsuarioRepository;

@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final EquipoRepository equipoRepository;
    private final PronosticoRepository pronosticoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    public PartidoService(
            PartidoRepository partidoRepository,
            EquipoRepository equipoRepository,
            PronosticoRepository pronosticoRepository,
            UsuarioRepository usuarioRepository,
            GrupoRepository grupoRepository) {

        this.partidoRepository = partidoRepository;
        this.equipoRepository = equipoRepository;
        this.pronosticoRepository = pronosticoRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    public List<Partido> listar() {
        return partidoRepository.findAll();
    }

    public List<Partido> listarPorFase(FaseMundial fase) {

        return partidoRepository.findByFase(fase);

    }

    public Partido buscarPorId(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
    }

    public Partido guardar(PartidoRequest request) {

        if (request.getLocalId().equals(request.getVisitanteId())) {
            throw new RuntimeException("Local y visitante no pueden ser el mismo equipo");
        }

        Grupo grupo = grupoRepository.findById(request.getGrupoId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        Equipo local = equipoRepository.findById(request.getLocalId())
                .orElseThrow(() -> new RuntimeException("Equipo local no encontrado"));

        Equipo visitante = equipoRepository.findById(request.getVisitanteId())
                .orElseThrow(() -> new RuntimeException("Equipo visitante no encontrado"));

        if (local.getGrupo() == null || visitante.getGrupo() == null) {
            throw new RuntimeException("Los equipos deben pertenecer a un grupo");
        }

        if (!local.getGrupo().getId().equals(grupo.getId())
                || !visitante.getGrupo().getId().equals(grupo.getId())) {

            throw new RuntimeException("Los equipos no pertenecen al grupo seleccionado");
        }

        Partido partido = new Partido();

        partido.setGrupo(grupo);
        partido.setLocal(local);
        partido.setVisitante(visitante);
        partido.setFechaHora(request.getFechaHora());
        partido.setFase(FaseMundial.GRUPOS);
        partido.setEstado(EstadoPartido.POR_JUGARSE);

        return partidoRepository.save(partido);
    }

    public Partido editar(Long id, PartidoRequest request) {

        Partido partido = buscarPorId(id);

        if (request.getLocalId().equals(request.getVisitanteId())) {
            throw new RuntimeException("Local y visitante no pueden ser el mismo equipo");
        }

        Grupo grupo = grupoRepository.findById(request.getGrupoId())
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        Equipo local = equipoRepository.findById(request.getLocalId())
                .orElseThrow(() -> new RuntimeException("Equipo local no encontrado"));

        Equipo visitante = equipoRepository.findById(request.getVisitanteId())
                .orElseThrow(() -> new RuntimeException("Equipo visitante no encontrado"));

        if (local.getGrupo() == null || visitante.getGrupo() == null) {
            throw new RuntimeException("Los equipos deben pertenecer a un grupo");
        }

        if (!local.getGrupo().getId().equals(grupo.getId())
                || !visitante.getGrupo().getId().equals(grupo.getId())) {

            throw new RuntimeException("Los equipos no pertenecen al grupo seleccionado");
        }

        partido.setGrupo(grupo);
        partido.setLocal(local);
        partido.setVisitante(visitante);
        partido.setFechaHora(request.getFechaHora());

        return partidoRepository.save(partido);
    }

    public void eliminar(Long id) {

        Partido partido = buscarPorId(id);

        if (partido.getEstado() != EstadoPartido.POR_JUGARSE) {
            throw new RuntimeException("Solo se pueden eliminar partidos por jugarse");
        }

        partidoRepository.delete(partido);
    }

    public Partido registrarResultado(Long partidoId, ResultadoRequest request) {

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

    private Tendencia calcularTendencia(Integer golesLocal, Integer golesVisitante) {

        if (golesLocal > golesVisitante) {
            return Tendencia.LOCAL;
        }

        if (golesVisitante > golesLocal) {
            return Tendencia.VISITANTE;
        }

        return Tendencia.EMPATE;
    }

    private void procesarPuntajes(Partido partido) {

        List<Pronostico> pronosticos = new ArrayList<>(
                pronosticoRepository.findByPartidoId(partido.getId()));

        for (Pronostico pronostico : pronosticos) {

            int puntos = calcularPuntos(pronostico, partido);

            pronostico.setPuntos(puntos);
            pronosticoRepository.save(pronostico);

            Usuario usuario = pronostico.getUsuario();

            if (usuario.getPuntos() == null) {
                usuario.setPuntos(0);
            }

            if (usuario.getPlenos() == null) {
                usuario.setPlenos(0);
            }

            usuario.setPuntos(usuario.getPuntos() + puntos);

            if (puntos == 3) {
                usuario.setPlenos(usuario.getPlenos() + 1);
            }

            usuarioRepository.save(usuario);
        }
    }

    private int calcularPuntos(Pronostico pronostico, Partido partido) {

        boolean resultadoExacto =
                pronostico.getGolesLocal().equals(partido.getGolesLocal())
                        && pronostico.getGolesVisitante().equals(partido.getGolesVisitante());

        if (resultadoExacto) {
            return 3;
        }

        Tendencia tendenciaPronostico = calcularTendencia(
                pronostico.getGolesLocal(),
                pronostico.getGolesVisitante());

        if (tendenciaPronostico == partido.getTendencia()) {
            return 1;
        }

        return 0;
    }

    public List<Partido> listarPorJugar() {

        return partidoRepository.findByEstadoOrderByFechaHoraAsc(
                EstadoPartido.POR_JUGARSE);
    }

    public boolean gruposFinalizados() {

        return partidoRepository
                .findByFaseAndEstado(
                        FaseMundial.GRUPOS,
                        EstadoPartido.POR_JUGARSE)
                .isEmpty();
    }

    public void simularPartidosPendientes() {

    List<Partido> partidos = partidoRepository
            .findByEstadoOrderByFechaHoraAsc(EstadoPartido.POR_JUGARSE);

    Random random = new Random();

    for (Partido partido : partidos) {

        ResultadoRequest resultado = new ResultadoRequest();

        resultado.setGolesLocal(random.nextInt(5));
        resultado.setGolesVisitante(random.nextInt(5));

        registrarResultado(partido.getId(), resultado);
    }
}
}
