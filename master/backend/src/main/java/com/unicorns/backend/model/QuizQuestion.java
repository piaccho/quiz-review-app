package com.unicorns.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
@Table(name = "quiz_question")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_content")
    private String questionContent;

    @Column(name = "correct_answers")
    private int questionCorrectAnswers;

    @Column(name = "wrong_answers")
    private int questionWrongAnswers;

    @ElementCollection
    @MapKeyColumn(name = "option")
    @Column(name = "correct_count")
    @CollectionTable(name = "correct_options_count", joinColumns = @JoinColumn(name = "quiz_question_id"))
    private Map<String, Integer> correctOptionsCount;

    @ElementCollection
    @MapKeyColumn(name = "option")
    @Column(name = "wrong_count")
    @CollectionTable(name = "wrong_options_count", joinColumns = @JoinColumn(name = "quiz_question_id"))
    private Map<String, Integer> wrongOptionsCount;

    // można zadbać o kolejność, najpierw poprawne, potem niepoprawne albo od najwiekszego procenta do najmniejszego
    public Map<String, Double> getOptionsPercentages() {
        Map<String, Double> optionsPercentages = new HashMap<>();
        int allOptionsCount = 0;

        Map<String, Integer> allOptionsCountMap = new HashMap<>(correctOptionsCount);
        wrongOptionsCount.forEach((key, value) -> allOptionsCountMap.merge(key, value, Integer::sum));

        for (Integer count : allOptionsCountMap.values()) {
            allOptionsCount += count;
        }

        for (Map.Entry<String, Integer> entry : allOptionsCountMap.entrySet()) {
            double optionPercentage = (double) entry.getValue() / allOptionsCount;
            optionsPercentages.put(entry.getKey(), optionPercentage);
        }

        return optionsPercentages;
    }
}
