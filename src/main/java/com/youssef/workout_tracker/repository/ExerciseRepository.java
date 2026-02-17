package com.youssef.workout_tracker.repository;

import com.youssef.workout_tracker.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByRoutineId(Long routineId);
}