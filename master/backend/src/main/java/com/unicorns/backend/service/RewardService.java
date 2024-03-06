package com.unicorns.backend.service;

import com.unicorns.backend.enums.Strategy;
import com.unicorns.backend.model.*;
import com.unicorns.backend.repository.QuizEntryRepository;
import com.unicorns.backend.repository.QuizRepository;
import com.unicorns.backend.strategy.RewardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RewardService {
    private final QuizService quizService;
    private final PresetService presetService;
    private final QuizEntryRepository quizEntryRepository;
    private final QuizRepository quizRepository;

    //TODO extract method, do not hardcode category values here, handle same time boundary if needed, handle max points exception better
    @Autowired
    public RewardService(QuizService quizService, PresetService presetService,
                         QuizEntryRepository quizEntryRepository, QuizRepository quizRepository) {
        this.quizService = quizService;
        this.presetService = presetService;
        this.quizEntryRepository = quizEntryRepository;
        this.quizRepository = quizRepository;
    }

    public Optional<Quiz> getResultsBasedOnStrategy(long quizId, long presetId)
    {
        Optional<Preset> optionalPreset = presetService.getPresetById(presetId);
        if (optionalPreset.isPresent()) {
            Strategy strategy = optionalPreset.get().getStrategy();
            List<PresetEntry> presetEntries = optionalPreset
                    .map(Preset::getPresetEntries)
                    .orElse(Collections.emptyList());

            Optional<Quiz> result = quizService.getQuizById(quizId);

            if (result.isPresent()) {
                new RewardManager(strategy).handleResult(result.get(), presetEntries);
                quizRepository.save(result.get());
                return result;
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

}
