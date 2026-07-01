package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.dto.AdminDashDTO;
import com.prode.dto.ProximoPartidoDTO;
import com.prode.entity.Partido;
import com.prode.enums.EstadoPartido;
import com.prode.repository.EquipoRepository;
import com.prode.repository.GrupoRepository;
import com.prode.repository.PartidoRepository;
import com.prode.repository.UsuarioRepository;
import com.prode.service.FaseService;

@RestController
@RequestMapping("/admin")
public class AdminApiController {

    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;
    private final GrupoRepository grupoRepository;
    private final PartidoRepository partidoRepository;
    private final FaseService faseService;

    public AdminApiController(
            UsuarioRepository usuarioRepository,
            EquipoRepository equipoRepository,
            GrupoRepository grupoRepository,
            PartidoRepository partidoRepository,
            FaseService faseService) {

        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
        this.grupoRepository = grupoRepository;
        this.partidoRepository = partidoRepository;
        this.faseService = faseService;
    }

    @GetMapping("/dashboard")
    public AdminDashDTO dashboard() {

        AdminDashDTO dto = new AdminDashDTO();

        dto.setUsuarios(usuarioRepository.count());
        dto.setEquipos(equipoRepository.count());
        dto.setGrupos(grupoRepository.count());
        dto.setTotalPartidos(partidoRepository.count());

        dto.setFaseActual(
                faseService.faseActual().name());

        long jugados = partidoRepository
                .findByEstadoOrderByFechaHoraAsc(
                        EstadoPartido.FINALIZADO)
                .size();

        long pendientes = partidoRepository
                .findByEstadoOrderByFechaHoraAsc(
                        EstadoPartido.POR_JUGARSE)
                .size();

        dto.setPartidosJugados(jugados);
        dto.setPartidosPendientes(pendientes);

        List<Partido> proximos =
                partidoRepository.findByEstadoOrderByFechaHoraAsc(
                        EstadoPartido.POR_JUGARSE);

        proximos.stream()
                .limit(5)
                .forEach(p -> {

                    ProximoPartidoDTO partido =
                            new ProximoPartidoDTO();

                    partido.setLocal(
                            p.getLocal().getNombre());

                    partido.setVisitante(
                            p.getVisitante().getNombre());

                    partido.setFecha(
                            p.getFechaHora().toString());

                    dto.getProximosPartidos()
                            .add(partido);

                });

        return dto;

    }

}
