package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.common.dto.ApiResponse;
import com.example.demo.common.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Usuario>> login(@RequestBody LoginRequest request) {
        Usuario usuario = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Bienvenido " + usuario.getNombre(), usuario));
    }
}