package com.prode.dto;

public class GrupoRequest {

    private String nombre;

    public GrupoRequest() {
    }

    public GrupoRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
