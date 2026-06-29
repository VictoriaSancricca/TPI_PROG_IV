package com.prode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingViewController {

    @GetMapping("/ranking")
    public String ranking() {
        return "ranking";
    }

}
