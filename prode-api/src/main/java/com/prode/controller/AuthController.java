package com.prode.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prode.dto.LoginRequest;
import com.prode.dto.LoginResponse;
import com.prode.dto.RegisterRequest;
import com.prode.entity.Usuario;
import com.prode.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(
            @RequestBody RegisterRequest request) {

        Usuario usuario = authService.registrar(request);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        System.out.println("ENTRO AL LOGIN");

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}
