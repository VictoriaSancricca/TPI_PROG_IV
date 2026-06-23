package com.prode.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.entity.Usuario;
import com.prode.service.RankingService;

@RestController
public class RankingController {

    private final RankingService rankingService;

    public RankingController(
            RankingService rankingService) {

        this.rankingService = rankingService;
    }

    @GetMapping("/ranking")
    public List<Usuario> ranking() {

        return rankingService.obtenerRanking();
    }
}
