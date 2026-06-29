package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PartidoViewController {

    @GetMapping("/partidos-view")
    public String partidos() {
        return "partidos";
    }

}
