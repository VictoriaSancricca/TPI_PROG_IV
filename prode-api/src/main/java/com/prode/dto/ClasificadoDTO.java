package com.prode.dto;

import com.prode.entity.Equipo;

public class ClasificadoDTO {

    private Equipo primero;
    private Equipo segundo; 
    
    public Equipo getPrimero() {
        return primero;
    }
    public void setPrimero(Equipo primero) {
        this.primero = primero;
    }
    public Equipo getSegundo() {
        return segundo;
    }
    public void setSegundo(Equipo segundo) {
        this.segundo = segundo;
    }

    
}
