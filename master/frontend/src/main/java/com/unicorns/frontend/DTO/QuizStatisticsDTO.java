package com.unicorns.frontend.DTO;

import lombok.Data;

import java.util.List;
@Data
public class QuizStatisticsDTO {
    private Long id;
    private List<QuizQuestionDTO> quizQuestions;
}