package com.youssef.workout_tracker.service;

import com.youssef.workout_tracker.exception.ResourceNotFoundException;
import com.youssef.workout_tracker.model.Routine;
import com.youssef.workout_tracker.model.User;
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
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public Routine createRoutine(String name, String description) {
        User user = getCurrentUser();

        Routine routine = Routine.builder()
                .name(name)
                .description(description)
                .user(user)
                .build();

        log.info("Usuario {} creó rutina: {}", user.getUsername(), name);
        return routineRepository.save(routine);
    }

    public List<Routine> getMyRoutines() {
        User user = getCurrentUser();
        return routineRepository.findByUserId(user.getId());
    }

    public Routine getRoutineById(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));

        User user = getCurrentUser();
        if (!routine.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("No tienes permiso para ver esta rutina");
        }

        return routine;
    }

    @Transactional
    public Routine updateRoutine(Long id, String name, String description) {
        Routine routine = getRoutineById(id);

        routine.setName(name);
        routine.setDescription(description);

        log.info("Rutina {} actualizada", id);
        return routineRepository.save(routine);
    }

    @Transactional
    public void deleteRoutine(Long id) {
        Routine routine = getRoutineById(id);
        routineRepository.delete(routine);
        log.info("Rutina {} eliminada", id);
    }

    // Para ADMIN
    public List<Routine> getAllRoutines() {
        return routineRepository.findAll();
    }
    public void deleteRoutineAdmin(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));
        routineRepository.delete(routine);
    }

    public Routine updateRoutineAdmin(Long id, Routine updatedRoutine) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada"));
        routine.setName(updatedRoutine.getName());
        routine.setDescription(updatedRoutine.getDescription());
        return routineRepository.save(routine);
    }
}
