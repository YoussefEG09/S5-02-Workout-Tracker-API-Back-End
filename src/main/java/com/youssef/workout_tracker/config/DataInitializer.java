package com.youssef.workout_tracker.config;

import com.youssef.workout_tracker.model.Role;
import com.youssef.workout_tracker.model.User;
import com.youssef.workout_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println(">>> Usuario ADMIN creado correctamente");
        } else {
            System.out.println(">>> Usuario ADMIN ya existe, no se crea de nuevo");
        }
    }
}
