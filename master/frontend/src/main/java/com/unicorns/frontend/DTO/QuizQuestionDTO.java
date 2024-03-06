package com.unicorns.frontend.DTO;

import lombok.Data;

import java.util.Map;
@Data
public class QuizQuestionDTO {
    private Long id;
    private String questionContent;
    private int questionCorrectAnswers;
    private int questionWrongAnswers;
    private Map<String, Integer> correctOptionsCount;
    private Map<String, Integer> wrongOptionsCount;
    private Map<String, Double> optionsPercentages;
}
