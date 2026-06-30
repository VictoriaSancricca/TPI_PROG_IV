package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EquiposViewController {

    @GetMapping("/admin/equipos")
    public String equipos() {
        return "equipos";
    }

}
