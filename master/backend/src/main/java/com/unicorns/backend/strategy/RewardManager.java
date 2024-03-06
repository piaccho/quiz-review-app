package com.unicorns.backend.strategy;

import com.unicorns.backend.enums.Strategy;
import com.unicorns.backend.model.PresetEntry;
import com.unicorns.backend.model.Quiz;

import java.util.List;

public class RewardManager {
    private RewardHandler rewardHandler;

    public RewardManager(Strategy strategy) {
        switch (strategy) {
            case POINTS:
                rewardHandler = new CorrectAnswersHandler();
                break;
            case TIME:
                rewardHandler = new BestPlayersHandler();
                break;
        }
    }

    public void handleResult(Quiz quiz, List<PresetEntry> presetEntries) {
        rewardHandler.handleReward(quiz, presetEntries);
    }
}
