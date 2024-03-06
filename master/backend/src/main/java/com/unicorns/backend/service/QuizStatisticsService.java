package com.unicorns.backend.service;

import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizStatistics;
import com.unicorns.backend.repository.QuizStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizStatisticsService {
    private final QuizService quizService;

    @Autowired
    public QuizStatisticsService(QuizService quizService) {
        this.quizService = quizService;
    }

    public Optional<QuizStatistics> getQuizStatisticsByQuizId(long id) {
        return quizService.getQuizById(id).map(Quiz::getQuizStatistics);
    }
}
