package com.youssef.workout_tracker.service;

import com.youssef.workout_tracker.exception.ResourceNotFoundException;
import com.youssef.workout_tracker.model.Exercise;
import com.youssef.workout_tracker.model.Routine;
import com.youssef.workout_tracker.model.User;
import com.youssef.workout_tracker.repository.ExerciseRepository;
import com.youssef.workout_tracker.repository.RoutineRepository;
import com.youssef.workout_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public Exercise createExercise(Long routineId, String name, int sets, int reps) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));

        User user = getCurrentUser();
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para añadir ejercicios a esta rutina");
        }

        Exercise exercise = Exercise.builder()
                .name(name)
                .sets(sets)
                .reps(reps)
                .routine(routine)
                .build();

        log.info("Usuario {} añadió ejercicio {} a rutina {}", user.getUsername(), name, routineId);
        return exerciseRepository.save(exercise);
    }

    public List<Exercise> getExercisesByRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));

        User user = getCurrentUser();
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para ver estos ejercicios");
        }

        return exerciseRepository.findByRoutineId(routineId);
    }

    @Transactional
    public Exercise updateExercise(Long id, String name, int sets, int reps) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        User user = getCurrentUser();
        if (!exercise.getRoutine().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para editar este ejercicio");
        }

        exercise.setName(name);
        exercise.setSets(sets);
        exercise.setReps(reps);

        log.info("Ejercicio {} actualizado", id);
        return exerciseRepository.save(exercise);
    }

    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        User user = getCurrentUser();
        if (!exercise.getRoutine().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para eliminar este ejercicio");
        }

        exerciseRepository.delete(exercise);
        log.info("Ejercicio {} eliminado", id);
    }
}
