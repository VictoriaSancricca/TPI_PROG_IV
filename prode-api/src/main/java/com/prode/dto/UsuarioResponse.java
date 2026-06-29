package com.prode.dto;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private Integer puntos;
    private Integer plenos;

    public UsuarioResponse() {
    }

    public UsuarioResponse(
            Long id,
            String nombre,
            String email,
            Integer puntos,
            Integer plenos) {

        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.puntos = puntos;
        this.plenos = plenos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getPlenos() {
        return plenos;
    }

    public void setPlenos(Integer plenos) {
        this.plenos = plenos;
    }

}
