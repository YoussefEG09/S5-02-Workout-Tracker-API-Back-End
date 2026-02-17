package com.youssef.workout_tracker.repository;

import com.youssef.workout_tracker.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByExerciseIdOrderByDateDesc(Long exerciseId);
}
