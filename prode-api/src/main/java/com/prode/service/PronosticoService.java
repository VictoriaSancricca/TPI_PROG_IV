package com.prode.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.PronosticoRequest;
import com.prode.entity.Partido;
import com.prode.entity.Pronostico;
import com.prode.entity.Usuario;
import com.prode.repository.PartidoRepository;
import com.prode.repository.PronosticoRepository;
import com.prode.repository.UsuarioRepository;

@Service
public class PronosticoService {

    private final PronosticoRepository pronosticoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PartidoRepository partidoRepository;

    public PronosticoService(
            PronosticoRepository pronosticoRepository,
            UsuarioRepository usuarioRepository,
            PartidoRepository partidoRepository) {

        this.pronosticoRepository = pronosticoRepository;
        this.usuarioRepository = usuarioRepository;
        this.partidoRepository = partidoRepository;
    }

    public List<Pronostico> listar() {
        return pronosticoRepository.findAll();
    }

    public Pronostico guardar(PronosticoRequest request) {

        Usuario usuario = usuarioRepository
                .findById(request.getUsuarioId())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        Partido partido = partidoRepository
                .findById(request.getPartidoId())
                .orElseThrow(() ->
                        new RuntimeException("Partido no encontrado"));

        boolean existe = pronosticoRepository
                .findByUsuarioIdAndPartidoId(
                        request.getUsuarioId(),
                        request.getPartidoId()
                )
                .isPresent();

        if (existe) {
            throw new RuntimeException(
                    "Ya existe un pronóstico para este partido");
        }

        LocalDateTime limite =
                partido.getFechaHora().minusMinutes(30);

        if (LocalDateTime.now().isAfter(limite)) {
            throw new RuntimeException(
                    "El plazo para pronosticar expiró");
        }

        Pronostico pronostico = new Pronostico();

        pronostico.setUsuario(usuario);
        pronostico.setPartido(partido);

        pronostico.setGolesLocal(
                request.getGolesLocal());

        pronostico.setGolesVisitante(
                request.getGolesVisitante());

        pronostico.setFechaCarga(
                LocalDateTime.now());

        pronostico.setPuntos(0);

        return pronosticoRepository.save(pronostico);
    }

    public List<Pronostico> listarPorUsuario(Long usuarioId) {
        return pronosticoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pronostico> listarPorPartido(Long partidoId) {
        return pronosticoRepository.findByPartidoId(partidoId);
    }
}
