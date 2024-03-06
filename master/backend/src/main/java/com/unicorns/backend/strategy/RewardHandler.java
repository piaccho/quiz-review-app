package com.unicorns.backend.strategy;

import com.unicorns.backend.model.*;

import java.util.List;

public abstract class RewardHandler {
    abstract void handleReward(Quiz result, List<PresetEntry> presetEntries);
    protected Prize assignPrize(QuizEntry result, PrizeCategory prizeCategory) {
        List<Prize> availablePrizes = prizeCategory.getAvailablePrizes();

        for (Prize preferredPrize : result.getPreferences()) {
            if (availablePrizes.contains(preferredPrize)) {
                return preferredPrize;
            }
        }
        return null;
    }

}
