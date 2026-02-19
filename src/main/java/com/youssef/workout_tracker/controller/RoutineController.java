package com.youssef.workout_tracker.controller;

import com.youssef.workout_tracker.model.Routine;
import com.youssef.workout_tracker.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
@Tag(name = "Rutinas", description = "Gestión de rutinas de entrenamiento (requiere autenticación)")
@SecurityRequirement(name = "Bearer Authentication")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    @Operation(summary = "Crear rutina", description = "Crea una nueva rutina asociada al usuario autenticado")
    public ResponseEntity<Routine> createRoutine(@RequestBody Map<String, String> body) {
        Routine routine = routineService.createRoutine(
                body.get("name"),
                body.get("description")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(routine);
    }

    @GetMapping
    @Operation(summary = "Listar mis rutinas", description = "Obtiene todas las rutinas del usuario autenticado")
    public ResponseEntity<List<Routine>> getMyRoutines() {
        return ResponseEntity.ok(routineService.getMyRoutines());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rutina por ID", description = "Obtiene una rutina específica si pertenece al usuario autenticado")
    public ResponseEntity<Routine> getRoutineById(@PathVariable Long id) {
        return ResponseEntity.ok(routineService.getRoutineById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rutina", description = "Modifica una rutina existente del usuario autenticado")
    public ResponseEntity<Routine> updateRoutine(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Routine routine = routineService.updateRoutine(
                id,
                body.get("name"),
                body.get("description")
        );
        return ResponseEntity.ok(routine);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rutina", description = "Elimina una rutina y todos sus ejercicios asociados")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }
}
