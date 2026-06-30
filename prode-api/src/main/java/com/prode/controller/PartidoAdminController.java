package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PartidoAdminController {

    @GetMapping("/partidos")
    public String partidos() {
        return "partidosAdmin";
    }

}
