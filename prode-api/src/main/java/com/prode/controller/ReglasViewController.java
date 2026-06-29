package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReglasViewController {

    @GetMapping("/reglas")
    public String reglas() {
        return "reglas";
    }

}
