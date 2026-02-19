package com.youssef.workout_tracker.service;

import com.youssef.workout_tracker.exception.ResourceNotFoundException;
import com.youssef.workout_tracker.model.Exercise;
import com.youssef.workout_tracker.model.Progress;
import com.youssef.workout_tracker.model.User;
import com.youssef.workout_tracker.repository.ExerciseRepository;
import com.youssef.workout_tracker.repository.ProgressRepository;
import com.youssef.workout_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public Progress createProgress(Long exerciseId, LocalDate date, String note) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        User user = getCurrentUser();
        if (!exercise.getRoutine().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para registrar progreso en este ejercicio");
        }

        Progress progress = Progress.builder()
                .date(date != null ? date : LocalDate.now())
                .note(note)
                .exercise(exercise)
                .build();

        log.info("Usuario {} registró progreso para ejercicio {}", user.getUsername(), exerciseId);
        return progressRepository.save(progress);
    }

    public List<Progress> getProgressByExercise(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        User user = getCurrentUser();
        if (!exercise.getRoutine().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para ver este progreso");
        }

        return progressRepository.findByExerciseIdOrderByDateDesc(exerciseId);
    }

    @Transactional
    public void deleteProgress(Long id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso no encontrado"));

        User user = getCurrentUser();
        if (!progress.getExercise().getRoutine().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para eliminar este progreso");
        }

        progressRepository.delete(progress);
        log.info("Progreso {} eliminado", id);
    }
}
