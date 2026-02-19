package com.youssef.workout_tracker.controller;

import com.youssef.workout_tracker.model.Progress;
import com.youssef.workout_tracker.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Tag(name = "Progreso", description = "Registro y seguimiento de progreso en ejercicios (requiere autenticación)")
@SecurityRequirement(name = "Bearer Authentication")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping
    @Operation(summary = "Registrar progreso", description = "Crea un nuevo registro de progreso para un ejercicio específico")
    public ResponseEntity<Progress> createProgress(@RequestBody Map<String, Object> body) {
        LocalDate date = body.containsKey("date")
                ? LocalDate.parse(body.get("date").toString())
                : LocalDate.now();

        Progress progress = progressService.createProgress(
                Long.valueOf(body.get("exerciseId").toString()),
                date,
                body.get("note") != null ? body.get("note").toString() : null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(progress);
    }

    @GetMapping("/exercise/{exerciseId}")
    @Operation(summary = "Ver historial de progreso", description = "Obtiene todos los registros de progreso de un ejercicio, ordenados por fecha descendente")
    public ResponseEntity<List<Progress>> getProgressByExercise(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(progressService.getProgressByExercise(exerciseId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de progreso", description = "Elimina un registro de progreso específico")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.noContent().build();
    }
}
