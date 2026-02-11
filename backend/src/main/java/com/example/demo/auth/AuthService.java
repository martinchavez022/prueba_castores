package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.common.entity.Usuario;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public Usuario login(LoginRequest request) {

        // 1. find user by email
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException("Correo o contraseña incorrectos"));

        // 2. check if account is active
        if (usuario.getEstatus() != 1) {
            throw new IllegalArgumentException("La cuenta está desactivada");
        }

        // 3. check password
        if (!usuario.getContrasena().equals(request.getContrasena())) {
            throw new ResourceNotFoundException("Correo o contraseña incorrectos");
        }

        return usuario;
    }
}