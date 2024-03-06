package com.unicorns.backend.repository;
import com.unicorns.backend.model.QuizEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizEntryRepository extends JpaRepository<QuizEntry, Long> {
}