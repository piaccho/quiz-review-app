package com.unicorns.backend.controller;

import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizStatistics;
import com.unicorns.backend.service.QuizService;
import com.unicorns.backend.service.QuizStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class QuizStatisticsController {
    private final QuizStatisticsService quizStatisticsService;

    @GetMapping("{quizId}")
    public ResponseEntity<QuizStatistics> getQuizStatisticsByQuizId(@PathVariable(name = "quizId") long quizId) {
        return quizStatisticsService.getQuizStatisticsByQuizId(quizId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
