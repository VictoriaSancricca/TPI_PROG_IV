package com.prode.dto;

import java.util.ArrayList;
import java.util.List;

public class AdminDashDTO { 

    private long usuarios;
    private long equipos;
    private long grupos;
    private long totalPartidos;
    private long partidosJugados;
    private long partidosPendientes;

    private String faseActual;

    private List<ProximoPartidoDTO> proximosPartidos = new ArrayList<>();

    public long getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(long usuarios) {
        this.usuarios = usuarios;
    }

    public long getEquipos() {
        return equipos;
    }

    public void setEquipos(long equipos) {
        this.equipos = equipos;
    }

    public long getGrupos() {
        return grupos;
    }

    public void setGrupos(long grupos) {
        this.grupos = grupos;
    }

    public long getTotalPartidos() {
        return totalPartidos;
    }

    public void setTotalPartidos(long totalPartidos) {
        this.totalPartidos = totalPartidos;
    }

    public long getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(long partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public long getPartidosPendientes() {
        return partidosPendientes;
    }

    public void setPartidosPendientes(long partidosPendientes) {
        this.partidosPendientes = partidosPendientes;
    }

    public String getFaseActual() {
        return faseActual;
    }

    public void setFaseActual(String faseActual) {
        this.faseActual = faseActual;
    }

    public List<ProximoPartidoDTO> getProximosPartidos() {
        return proximosPartidos;
    }

    public void setProximosPartidos(List<ProximoPartidoDTO> proximosPartidos) {
        this.proximosPartidos = proximosPartidos;
    }
}
