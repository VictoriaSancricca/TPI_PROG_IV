package com.prode.entity;

import com.prode.enums.FaseMundial;

import jakarta.persistence.*;

@Entity
@Table(name = "fases")
public class Fase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FaseMundial faseActual;

    public Long getId() {
        return id;
    }

    public FaseMundial getFaseActual() {
        return faseActual;
    }

    public void setFaseActual(FaseMundial faseActual) {
        this.faseActual = faseActual;
    }

}
