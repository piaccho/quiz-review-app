package com.unicorns.backend.service;

import com.unicorns.backend.model.Prize;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizEntry;
import com.unicorns.backend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final PrizeService prizeService;

    @Autowired
    public QuizService(QuizRepository quizRepository, PrizeService prizeService) {
        this.quizRepository = quizRepository;
        this.prizeService = prizeService;
    }

    public Optional<Quiz> getQuizById(long id) {
        return quizRepository.findById(id);
    }
    public Optional<List<QuizEntry>> getQuizEntriesByQuizId(long id) {
        return quizRepository.findById(id).map(Quiz::getQuizEntries);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<QuizEntry> modifyStudent(Long prizeId, Long studentId, Long quizId) throws ChangeSetPersister.NotFoundException {
        Prize prize = prizeService.getPrizeById(prizeId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return getQuizById(quizId).map(quiz -> {
            Optional<QuizEntry> quizEntryOptional = quiz.getQuizEntries().stream()
                    .filter(quizEntry -> quizEntry.getId().equals(studentId))
                    .findFirst();

            if (quizEntryOptional.isPresent()) {
                quizEntryOptional.get().setPrize(prize);
                quizRepository.save(quiz);
                return quizEntryOptional.get();
            }
            return null;
        });

    }
}