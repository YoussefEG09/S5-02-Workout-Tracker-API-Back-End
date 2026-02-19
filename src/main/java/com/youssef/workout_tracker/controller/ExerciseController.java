package com.youssef.workout_tracker.controller;

import com.youssef.workout_tracker.model.Exercise;
import com.youssef.workout_tracker.service.ExerciseService;
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
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Tag(name = "Ejercicios", description = "Gestión de ejercicios (requiere autenticación)")
@SecurityRequirement(name = "Bearer Authentication")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    @Operation(summary = "Crear ejercicio", description = "Crea un nuevo ejercicio asociado al usuario autenticado")
    public ResponseEntity<Exercise> createExercise(@RequestBody Map<String, Object> body) {
        Exercise exercise = exerciseService.createExercise(
                Long.valueOf(body.get("routineId").toString()),
                body.get("name").toString(),
                Integer.parseInt(body.get("sets").toString()),
                Integer.parseInt(body.get("reps").toString())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
    }

    @GetMapping("/routine/{routineId}")
    @Operation(summary = "Listar ejercicios a partir de una rutina", description = "Muestra un ejercicio dentro de una rutina asociada al usuario autenticado")
    public ResponseEntity<List<Exercise>> getExercisesByRoutine(@PathVariable Long routineId) {
        return ResponseEntity.ok(exerciseService.getExercisesByRoutine(routineId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ejercicio", description = "Modifica un ejercicio existente del usuario autenticado")
    public ResponseEntity<Exercise> updateExercise(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Exercise exercise = exerciseService.updateExercise(
                id,
                body.get("name").toString(),
                Integer.parseInt(body.get("sets").toString()),
                Integer.parseInt(body.get("reps").toString())
        );
        return ResponseEntity.ok(exercise);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ejercicio", description = "Elimina un ejercicio del usuario autenticado")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
