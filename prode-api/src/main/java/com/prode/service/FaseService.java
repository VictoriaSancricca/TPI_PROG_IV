package com.prode.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prode.dto.ClasificadoGrupoDTO;
import com.prode.dto.GrupoResponse;
import com.prode.dto.TablaPosicionesDTO;
import com.prode.entity.Equipo;
import com.prode.entity.Fase;
import com.prode.entity.Grupo;
import com.prode.entity.Partido;
import com.prode.enums.EstadoPartido;
import com.prode.enums.FaseMundial;
import com.prode.repository.FaseRepository;
import com.prode.repository.PartidoRepository;
@Service
public class FaseService {

    private final FaseRepository faseRepository;
    private final GrupoService grupoService;
    private final PartidoService partidoService;
    private final PartidoRepository partidoRepository;
    private LocalDate fechaActual = LocalDate.now();

    private final LocalTime[] horarios = {

            LocalTime.of(13, 0),
            LocalTime.of(16, 0),
            LocalTime.of(19, 0),
            LocalTime.of(22, 0)

    };

    private int indiceHorario = 0;

    public FaseService(
        FaseRepository faseRepository,
        GrupoService grupoService,
        PartidoService partidoService,
        PartidoRepository partidoRepository) {

        this.faseRepository = faseRepository;
        this.grupoService = grupoService;
        this.partidoService = partidoService;
        this.partidoRepository = partidoRepository;
    }

    public Fase obtenerFase() {

        List<Fase> fases = faseRepository.findAll();

        if (fases.isEmpty()) {

            Fase fase = new Fase();

            fase.setFaseActual(FaseMundial.GRUPOS);

            return faseRepository.save(fase);

        }

        return fases.get(0);

    }

    public FaseMundial faseActual() {

        return obtenerFase().getFaseActual();

    }

    public void cambiarFase(FaseMundial faseNueva) {

        Fase fase = obtenerFase();

        fase.setFaseActual(faseNueva);

        faseRepository.save(fase);

    }

    public void generarPreliminar() {

        if (!partidoService.gruposFinalizados()) {

            throw new RuntimeException(
                    "Todavía existen partidos de grupos sin finalizar.");

        }

        List<ClasificadoGrupoDTO> clasificados =
                obtenerClasificadosPorGrupo();

        List<TablaPosicionesDTO> primeros =
                ordenarPrimeros(
                        obtenerPrimeros(clasificados));

        List<TablaPosicionesDTO> segundos =
                obtenerSegundos(clasificados);

        // Los 8 mejores primeros pasan directamente
        List<TablaPosicionesDTO> mejoresPrimeros =
                primeros.subList(0, 8);

        // Los otros 4 primeros juegan la preliminar
        List<TablaPosicionesDTO> restantesPrimeros =
                primeros.subList(8, 12);

        // Primeros restantes vs primeros cuatro segundos

        for (int i = 0; i < 4; i++) {

            Equipo local = grupoService.buscarEquipo(
                    restantesPrimeros.get(i).getIdEquipo());

            Equipo visitante = grupoService.buscarEquipo(
                    segundos.get(i).getIdEquipo());

            crearPartido(
                    local,
                    visitante,
                    null,
                    FaseMundial.PRELIMINAR);

        }

        // Los otros ocho segundos juegan entre sí

        for (int i = 4; i < 12; i += 2) {

            Equipo local = grupoService.buscarEquipo(
                    segundos.get(i).getIdEquipo());

            Equipo visitante = grupoService.buscarEquipo(
                    segundos.get(i + 1).getIdEquipo());

            crearPartido(
                    local,
                    visitante,
                    null,
                    FaseMundial.PRELIMINAR);

        }

        cambiarFase(FaseMundial.PRELIMINAR);

    }

    private void crearPartido(
        Equipo local,
        Equipo visitante,
        Grupo grupo,
        FaseMundial fase) {

        Partido partido = new Partido();

        partido.setLocal(local);

        partido.setVisitante(visitante);

        partido.setGrupo(grupo);

        partido.setEstado(EstadoPartido.POR_JUGARSE);

        partido.setFase(fase);

        partido.setFechaHora(
                LocalDateTime.of(
                        fechaActual,
                        horarios[indiceHorario]
                )
        );

        indiceHorario++;

        if (indiceHorario == horarios.length) {

            indiceHorario = 0;

            fechaActual = fechaActual.plusDays(1);

        }

        partidoRepository.save(partido);

    }

    public void generarPartidosGrupos() {

        fechaActual = LocalDate.now();
        indiceHorario = 0;
        
        if (!partidoRepository.findAll().isEmpty()) {

            throw new RuntimeException(
                    "Los partidos ya fueron generados.");

        }
        List<GrupoResponse> grupos = grupoService.listar();

        for (GrupoResponse grupoDTO : grupos) {

            Grupo grupo = grupoService.buscarPorId(grupoDTO.getId());

            List<Equipo> equipos = grupo.getEquipos();

            if (equipos.size() != 4) {

                throw new RuntimeException(
                        "El grupo " + grupo.getNombre()
                        + " no tiene exactamente 4 equipos.");

            }

            for (int i = 0; i < equipos.size(); i++) {

                for (int j = i + 1; j < equipos.size(); j++) {

                    crearPartido(

                            equipos.get(i),

                            equipos.get(j),

                            grupo,

                            FaseMundial.GRUPOS

                    );

                }

            }

        }

    }

    private List<Equipo> obtenerClasificados() {

        List<Equipo> clasificados = new ArrayList<>();

        List<GrupoResponse> grupos = grupoService.listar();

        for (GrupoResponse grupo : grupos) {

            List<TablaPosicionesDTO> tabla =
                    grupoService.calcularTablaGrupo(grupo.getId());

            clasificados.add(
                    grupoService.buscarEquipo(
                            tabla.get(0).getIdEquipo()));

            clasificados.add(
                    grupoService.buscarEquipo(
                            tabla.get(1).getIdEquipo()));

        }

        return clasificados;

    }

    private void generarCrucesOctavos(List<Equipo> clasificados) {

        for (int i = 0; i < clasificados.size(); i += 2) {

            Equipo local = clasificados.get(i);

            Equipo visitante = clasificados.get(i + 1);

            crearPartido(
                    local,
                    visitante,
                    null,
                    FaseMundial.OCTAVOS);

        }

    }

    public void generarSiguienteFase() {

    // Si todavía no hay partidos, genera automáticamente la fase de grupos
    if (partidoRepository.count() == 0) {

        generarPartidosGrupos();

        return;

    }

    switch (faseActual()) {

        case GRUPOS:

            generarPreliminar();

            break;

        case PRELIMINAR:

            generarOctavos();

            break;

        case OCTAVOS:

            generarCuartos();

            break;

        case CUARTOS:

            generarSemifinal();

            break;

        case SEMIFINAL:

            generarFinal();

            break;

        default:

            throw new RuntimeException(
                    "No hay más fases para generar.");

    }

}

    private List<ClasificadoGrupoDTO> obtenerClasificadosPorGrupo() {

        List<ClasificadoGrupoDTO> clasificados = new ArrayList<>();

        List<GrupoResponse> grupos = grupoService.listar();

        for (GrupoResponse grupo : grupos) {

            List<TablaPosicionesDTO> tabla =
                    grupoService.calcularTablaGrupo(grupo.getId());

            ClasificadoGrupoDTO dto = new ClasificadoGrupoDTO();

            dto.setGrupo(grupo.getNombre());

            dto.setPrimero(tabla.get(0));

            dto.setSegundo(tabla.get(1));

            clasificados.add(dto);

        }

        return clasificados;

    }

    private List<TablaPosicionesDTO> obtenerPrimeros(

        List<ClasificadoGrupoDTO> clasificados){

        List<TablaPosicionesDTO> primeros =
                new ArrayList<>();

        for(ClasificadoGrupoDTO grupo : clasificados){

            primeros.add(grupo.getPrimero());

        }

        return primeros;

    }

    private List<TablaPosicionesDTO> obtenerSegundos(

        List<ClasificadoGrupoDTO> clasificados){

        List<TablaPosicionesDTO> segundos =
                new ArrayList<>();

        for(ClasificadoGrupoDTO grupo : clasificados){

            segundos.add(grupo.getSegundo());

        }

        return segundos;

    }

    private List<TablaPosicionesDTO> ordenarPrimeros(
        List<TablaPosicionesDTO> primeros) {

        primeros.sort((a, b) -> {

            if (a.getPuntos() != b.getPuntos()) {
                return b.getPuntos() - a.getPuntos();
            }

            if (a.getDg() != b.getDg()) {
                return b.getDg() - a.getDg();
            }

            return b.getGf() - a.getGf();

        });

        return primeros;

    }

    private List<Equipo> obtenerGanadores(FaseMundial fase) {

        List<Partido> partidos = partidoRepository.findByFaseAndEstado(
                fase,
                EstadoPartido.FINALIZADO);

        List<Equipo> ganadores = new ArrayList<>();

        for (Partido partido : partidos) {

            if (partido.getGolesLocal() > partido.getGolesVisitante()) {

                ganadores.add(partido.getLocal());

            } else {

                ganadores.add(partido.getVisitante());

            }

        }

        return ganadores;

    }

    private void generarFaseEliminatoria(
        FaseMundial faseAnterior,
        FaseMundial faseNueva) {

        List<Partido> partidos = partidoRepository.findByFaseAndEstado(
                faseAnterior,
                EstadoPartido.FINALIZADO);

        if (partidos.isEmpty()) {

            throw new RuntimeException(
                    "No existen partidos finalizados de la fase "
                            + faseAnterior);

        }

        List<Equipo> ganadores = obtenerGanadores(faseAnterior);

        if (ganadores.size() % 2 != 0) {

            throw new RuntimeException(
                    "Cantidad de equipos inválida para generar la fase.");

        }

        for (int i = 0; i < ganadores.size(); i += 2) {

            crearPartido(
                    ganadores.get(i),
                    ganadores.get(i + 1),
                    null,
                    faseNueva);

        }

        cambiarFase(faseNueva);

    }

    public void generarOctavos() {

        List<ClasificadoGrupoDTO> clasificados =
                obtenerClasificadosPorGrupo();

        List<TablaPosicionesDTO> primeros =
                ordenarPrimeros(
                        obtenerPrimeros(clasificados));

        // Los 8 mejores primeros
        List<TablaPosicionesDTO> mejoresPrimeros =
                primeros.subList(0, 8);

        // Ganadores de la preliminar
        List<Equipo> ganadoresPreliminar =
                obtenerGanadores(FaseMundial.PRELIMINAR);

        if (ganadoresPreliminar.size() != 8) {

            throw new RuntimeException(
                    "Deben existir 8 ganadores de la fase preliminar.");

        }

        List<Equipo> clasificadosOctavos =
                new ArrayList<>();

        // Agrego los 8 mejores primeros

        for (TablaPosicionesDTO equipo : mejoresPrimeros) {

            clasificadosOctavos.add(

                    grupoService.buscarEquipo(
                            equipo.getIdEquipo())

            );

        }

        // Agrego los 8 ganadores de preliminar

        clasificadosOctavos.addAll(
                ganadoresPreliminar);

        // Genero los 8 partidos

        for (int i = 0; i < clasificadosOctavos.size(); i += 2) {

            crearPartido(

                    clasificadosOctavos.get(i),

                    clasificadosOctavos.get(i + 1),

                    null,

                    FaseMundial.OCTAVOS

            );

        }

        cambiarFase(FaseMundial.OCTAVOS);

    }

    public void generarCuartos() {

        generarFaseEliminatoria(

                FaseMundial.OCTAVOS,

                FaseMundial.CUARTOS

        );

    }

    public void generarSemifinal() {

        generarFaseEliminatoria(

                FaseMundial.CUARTOS,

                FaseMundial.SEMIFINAL

        );

    }

    public void generarFinal() {

        List<Partido> semifinales =
                partidoRepository.findByFaseAndEstado(
                        FaseMundial.SEMIFINAL,
                        EstadoPartido.FINALIZADO);

        if (semifinales.size() != 2) {

            throw new RuntimeException(
                    "Deben existir dos semifinales finalizadas.");

        }

        Equipo ganador1;
        Equipo ganador2;

        Equipo perdedor1;
        Equipo perdedor2;

        Partido semi1 = semifinales.get(0);

        if (semi1.getGolesLocal() > semi1.getGolesVisitante()) {

            ganador1 = semi1.getLocal();
            perdedor1 = semi1.getVisitante();

        } else {

            ganador1 = semi1.getVisitante();
            perdedor1 = semi1.getLocal();

        }

        Partido semi2 = semifinales.get(1);

        if (semi2.getGolesLocal() > semi2.getGolesVisitante()) {

            ganador2 = semi2.getLocal();
            perdedor2 = semi2.getVisitante();

        } else {

            ganador2 = semi2.getVisitante();
            perdedor2 = semi2.getLocal();

        }

        crearPartido(

                ganador1,

                ganador2,

                null,

                FaseMundial.FINAL

        );

        crearPartido(

                perdedor1,

                perdedor2,

                null,

                FaseMundial.TERCER_PUESTO

        );

        cambiarFase(FaseMundial.FINAL);

    }
}
