package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PronosticoViewController {

    @GetMapping("/mis-pronosticos")
    public String pronosticos() {
        return "pronosticos";
    }

}