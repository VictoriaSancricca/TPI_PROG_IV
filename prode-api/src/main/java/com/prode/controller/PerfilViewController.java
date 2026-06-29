package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PerfilViewController {

    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }

}
