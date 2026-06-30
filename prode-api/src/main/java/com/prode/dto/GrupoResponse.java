package com.prode.dto;
import java.util.List;

public class GrupoResponse {

    private Long id;

    private String nombre;

    private List<EquipoResumenDTO> equipos;

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

    public List<EquipoResumenDTO> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<EquipoResumenDTO> equipos) {
        this.equipos = equipos;
    }

    
}
