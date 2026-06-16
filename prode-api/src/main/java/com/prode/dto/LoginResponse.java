package com.prode.dto;

public class LoginResponse {

    private String mensaje;
    private String nombre;
    private String rol;

    public LoginResponse() {
    }

    public LoginResponse(String mensaje, String nombre, String rol) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
