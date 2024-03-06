package com.unicorns.backend.strategy;

import com.unicorns.backend.model.*;

import java.util.Comparator;
import java.util.List;

public class BestPlayersHandler extends RewardHandler{
    @Override
    public void handleReward(Quiz quiz, List<PresetEntry> presetEntries) {
        List<QuizEntry> sortedQuizEntries = quiz.getQuizEntries().stream()
                .sorted(Comparator.comparing(QuizEntry::getCompletingTime))
                .toList();

        int maxPoints = quiz.getQuizEntries().stream()
                .map(QuizEntry::getTotalPoints)
                .max(Integer::compareTo).orElse(-1);

        long numberOfQuizEntries = quiz.getQuizEntries().size();

        int availableNumberOfPeopleWithBestPrize = (int) (numberOfQuizEntries * presetEntries.get(0).getValue());

        int counter = 0;
        for (QuizEntry entry: sortedQuizEntries) {
            if ((counter < availableNumberOfPeopleWithBestPrize) && (maxPoints == entry.getTotalPoints())) {
                entry.setPrize(assignPrize(entry, presetEntries.get(0).getPrizeCategory()));
                counter++;
            } else {
                entry.setPrize(assignPrize(entry, presetEntries.get(1).getPrizeCategory()));
            }
        }
    }

}
