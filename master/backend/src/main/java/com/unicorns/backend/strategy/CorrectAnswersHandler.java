package com.unicorns.backend.strategy;

import com.unicorns.backend.model.PresetEntry;
import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizEntry;

import java.util.List;

public class CorrectAnswersHandler extends RewardHandler {

    @Override
    public void handleReward(Quiz quiz, List<PresetEntry> presetEntries) {
        for (QuizEntry entry: quiz.getQuizEntries()) {
            int points = entry.getTotalPoints();
            int flag = 0;
            for (int i = presetEntries.size() - 1; i >= 0; i--) {
                PresetEntry categoryWithValue = presetEntries.get(i);
                if (points >= categoryWithValue.getValue()) {
                    entry.setPrize(assignPrize(entry, categoryWithValue.getPrizeCategory()));
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                entry.setPrize(null);
            }
        }
    }
}
