package com.prode.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prode.dto.LoginRequest;
import com.prode.dto.LoginResponse;
import com.prode.dto.RegisterRequest;
import com.prode.entity.Rol;
import com.prode.entity.Usuario;
import com.prode.enums.RolNombre;
import com.prode.repository.RolRepository;
import com.prode.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(RegisterRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rolUser = rolRepository
                .findByNombre(RolNombre.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());

        usuario.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        usuario.setRol(rolUser);

        return usuarioRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        boolean coincide = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        );

        if (!coincide) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse(
                "Login correcto",
                usuario.getNombre(),
                usuario.getRol().getNombre().name()
        );
    }
}
