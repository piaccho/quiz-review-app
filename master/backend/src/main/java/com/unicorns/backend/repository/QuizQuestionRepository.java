package com.unicorns.backend.repository;
import com.unicorns.backend.model.QuizQuestion;
import com.unicorns.backend.model.QuizStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}