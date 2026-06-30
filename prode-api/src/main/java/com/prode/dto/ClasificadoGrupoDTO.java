package com.prode.dto;
import com.prode.dto.TablaPosicionesDTO;

public class ClasificadoGrupoDTO {

    private String grupo;

    private TablaPosicionesDTO primero;

    private TablaPosicionesDTO segundo;

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public TablaPosicionesDTO getPrimero() {
        return primero;
    }

    public void setPrimero(TablaPosicionesDTO primero) {
        this.primero = primero;
    }

    public TablaPosicionesDTO getSegundo() {
        return segundo;
    }

    public void setSegundo(TablaPosicionesDTO segundo) {
        this.segundo = segundo;
    }

}
