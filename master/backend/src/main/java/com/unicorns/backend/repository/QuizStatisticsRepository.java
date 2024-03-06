package com.unicorns.backend.repository;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizStatisticsRepository extends JpaRepository<QuizStatistics, Long> {
}