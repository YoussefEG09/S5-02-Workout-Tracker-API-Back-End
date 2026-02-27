package com.youssef.workout_tracker.service;

import com.youssef.workout_tracker.dto.AuthRequest;
import com.youssef.workout_tracker.dto.AuthResponse;
import com.youssef.workout_tracker.exception.ResourceNotFoundException;
import com.youssef.workout_tracker.model.Role;
import com.youssef.workout_tracker.model.User;
import com.youssef.workout_tracker.repository.UserRepository;
import com.youssef.workout_tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceNotFoundException("El usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        log.info("Nuevo usuario registrado: {}", request.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getId());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        System.out.println("🟢 AuthService - Intentando autenticar: " + request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            System.out.println("✅ AuthService - Autenticación exitosa");
        } catch (Exception e) {
            System.out.println("❌ AuthService - Error en autenticación: " + e.getMessage());
            throw e;
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        log.info("Login exitoso: {}", request.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), user.getId());
        return new AuthResponse(token);
    }
}
