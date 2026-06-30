package com.prode.dto;

import java.time.LocalDateTime;

public class PartidoRequest {

    private Long fechaId;
    private Long localId;
    private Long visitanteId;
    private LocalDateTime fechaHora;
    private Long grupoId;

    public Long getFechaId() {
        return fechaId;
    }

    public void setFechaId(Long fechaId) {
        this.fechaId = fechaId;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    
}
