package com.prode.entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prode.enums.EstadoFecha;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fechas")
public class Fecha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private EstadoFecha estado;

    @JsonIgnore
    @OneToMany(mappedBy = "fecha")
    private List<Partido> partidos;

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

    public EstadoFecha getEstado() {
        return estado;
    }

    public void setEstado(EstadoFecha estado) {
        this.estado = estado;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    
}
