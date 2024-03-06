package com.unicorns.backend.service;

import com.unicorns.backend.model.Prize;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizEntry;
import com.unicorns.backend.repository.PrizeRepository;
import com.unicorns.backend.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class QuizServiceTest {


    private QuizService underTest;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private PrizeService prizeService;


    @BeforeEach
    void setUp() {
        underTest = new QuizService(quizRepository, prizeService);
    }
    @Test
    void getQuizByIdWhenQuizExists() {
        //given
        long quizId = 1;
        Quiz expectedQuiz = new Quiz();
        expectedQuiz.setId(quizId);
        given(quizRepository.findById(quizId)).willReturn(Optional.of(expectedQuiz));

        //when
        Optional<Quiz> actualQuiz =  underTest.getQuizById(quizId);

        //then
        assertTrue(actualQuiz.isPresent());
        assertEquals(expectedQuiz, actualQuiz.get());
        verify(quizRepository).findById(quizId);

    }
    @Test
    void getQuizByIdWhenQuizNotExist()
    {
        //given
        long quizId = 1;
        given(quizRepository.findById(quizId)).willReturn(Optional.empty());

        //when
        Optional<Quiz> actualQuiz = underTest.getQuizById(quizId);

        //then

        assertFalse(actualQuiz.isPresent());
        verify(quizRepository).findById(quizId);


    }
    @Test
    void getQuizEntriesByQuizIdWhenQuizExists() {

        //given
        long quizId = 1;
        Quiz expectedQuiz = new Quiz();
        QuizEntry expectedQuizEntries = new QuizEntry();
        expectedQuiz.setQuizEntries(List.of(expectedQuizEntries));
        given(quizRepository.findById(quizId)).willReturn(Optional.of(expectedQuiz));
        //when
        Optional<List<QuizEntry>> actualEntries = underTest.getQuizEntriesByQuizId(quizId);

        //then

        assertTrue(actualEntries.isPresent());
        assertEquals(List.of(expectedQuizEntries), actualEntries.get());
        verify(quizRepository).findById(quizId);

    }

    @Test
    void getQuizEntriesByQuizIdWhenQuizNotExists() {

        //given
        long quizId = 1;

        given(quizRepository.findById(quizId)).willReturn(Optional.empty());
        //when
        Optional<List<QuizEntry>> actualEntries = underTest.getQuizEntriesByQuizId(quizId);
        //then
        assertFalse(actualEntries.isPresent());
        verify(quizRepository).findById(quizId);

    }

    @Test
    void shouldGetAllQuizzes() {
        underTest.getAllQuizzes();
        verify(quizRepository).findAll();
    }
}