package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GrupoViewController {

    @GetMapping("/admin/grupos")
    public String grupos() {

        return "grupos";

    }

}
