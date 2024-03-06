package com.unicorns.backend.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
@Table(name = "quiz_statistics")
public class QuizStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "quiz_id")
//    private Quiz quiz;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QuizQuestion> quizQuestions;

    public List<Double> getCorrectAnswersPercentages() {
        List<Double> correctAnswersPercentages = new ArrayList<>();
        for (QuizQuestion quizQuestion : quizQuestions) {
            double correctAnswersPercentage = (double) quizQuestion.getQuestionCorrectAnswers() / (quizQuestion.getQuestionCorrectAnswers() + quizQuestion.getQuestionWrongAnswers());
            correctAnswersPercentages.add(correctAnswersPercentage);
        }
        return correctAnswersPercentages;
    }

}
