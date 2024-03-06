package com.unicorns.backend.service;

import com.unicorns.backend.enums.Strategy;
import com.unicorns.backend.model.Preset;
import com.unicorns.backend.model.PresetEntry;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.repository.QuizEntryRepository;
import com.unicorns.backend.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class RewardServiceTest {

    private RewardService underTest;

    @Mock
    private  QuizService quizService;
    @Mock
    private  PresetService presetService;
    @Mock
    private  QuizEntryRepository quizEntryRepository;
    @Mock
    private  QuizRepository quizRepository;

    @BeforeEach
    void setUp() {
        underTest = new RewardService(quizService,presetService,quizEntryRepository,quizRepository);
    }


    @Test
    void GetResultsWithPointsStrategy() {

        //given
        long presetId = 1;
        long quizId = 1;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        Preset preset = new Preset();
        PresetEntry presetEntry = new PresetEntry();
        preset.setPresetEntries(List.of(presetEntry));
        preset.setStrategy(Strategy.POINTS);
        given(presetService.getPresetById(presetId)).willReturn(Optional.of(preset));
        given(quizService.getQuizById(quizId)).willReturn(Optional.of(quiz));
        //when
        Optional<Quiz> actualQuiz = underTest.getResultsBasedOnStrategy(quizId,presetId);
        //then
        assertTrue(actualQuiz.isPresent());
    }

    @Test
    void GetResultsWithTimeStrategy() {

        //given
        long presetId = 1;
        long quizId = 1;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        Preset preset = new Preset();
        PresetEntry presetEntry = new PresetEntry();
        preset.setPresetEntries(List.of(presetEntry));
        preset.setStrategy(Strategy.TIME);
        given(presetService.getPresetById(presetId)).willReturn(Optional.of(preset));
        given(quizService.getQuizById(quizId)).willReturn(Optional.of(quiz));

        //when
        Optional<Quiz> actualQuiz = underTest.getResultsBasedOnStrategy(quizId,presetId);

        //then
        assertTrue(actualQuiz.isPresent());
    }

    @Test
    void GetResultsWithAbsentPreset() {

        //given
        long presetId = 1;
        long quizId = 1;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        given(presetService.getPresetById(presetId)).willReturn(Optional.empty());
        given(quizService.getQuizById(quizId)).willReturn(Optional.of(quiz));
        //when
        Optional<Quiz> actualQuiz = underTest.getResultsBasedOnStrategy(quizId,presetId);
        //then
        assertFalse(actualQuiz.isPresent());
    }

    @Test
    void GetResultsWithAbsentQuiz() {
        long presetId = 1;
        long quizId = 1;

        Preset preset = new Preset();
        PresetEntry presetEntry = new PresetEntry();
        preset.setPresetEntries(List.of(presetEntry));
        preset.setStrategy(Strategy.POINTS);
        given(presetService.getPresetById(presetId)).willReturn(Optional.of(preset));
        given(quizService.getQuizById(quizId)).willReturn(Optional.empty());
        //when
        Optional<Quiz> actualQuiz = underTest.getResultsBasedOnStrategy(quizId,presetId);
        //then
        assertFalse(actualQuiz.isPresent());

    }
}