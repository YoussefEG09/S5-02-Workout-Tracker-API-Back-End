package com.youssef.workout_tracker.controller;

import com.youssef.workout_tracker.dto.AuthRequest;
import com.youssef.workout_tracker.dto.AuthResponse;
import com.youssef.workout_tracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints públicos para registro y login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario con rol USER y devuelve un token JWT")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT válido por 24 horas")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("🔵 AuthController - Login request recibido para: " + request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }


}
