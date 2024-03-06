package com.unicorns.backend.controller;

import com.unicorns.backend.config.BackendConfig;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.model.QuizEntry;
import com.unicorns.backend.model.QuizStatistics;
import com.unicorns.backend.service.ExcelFileProcessService;
import com.unicorns.backend.service.QuizService;
import com.unicorns.backend.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final RewardService rewardService;


    private final ExcelFileProcessService excelFileProcessService;
    private final BackendConfig config;

    @PostMapping()
    public ResponseEntity<Long> createQuiz(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty() || !file.getOriginalFilename().endsWith(config.getUploadRequiredFileExtension())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Quiz quizOut;
        try {
            quizOut = excelFileProcessService.getQuizFromFile(file);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(quizOut.getId());
    }

    @PostMapping("results/{quizId}/{presetId}")
    public ResponseEntity<Long> createQuizResultBasedOnStrategy(@PathVariable(name = "quizId") long quizId,
                                                                       @PathVariable(name = "presetId") long presetId) {

        try {

            return rewardService.getResultsBasedOnStrategy(quizId, presetId)
                    .map(Quiz::getId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("student-modify")
    public ResponseEntity<Long> modifyStudent(@RequestBody Map<String, Object> requestBody) {
        try {
            Long prizeId = Long.parseLong(requestBody.get("prizeId").toString()); //TODO: or better new class?
            Long studentId = Long.parseLong(requestBody.get("studentId").toString());
            Long quizId = Long.parseLong(requestBody.get("quizId").toString());
            return quizService.modifyStudent(prizeId, studentId, quizId)
                    .map(QuizEntry::getId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("{id}/entries")
    public ResponseEntity<List<QuizEntry>> getQuizEntriesById(@PathVariable(name = "id") long id) {
        return quizService.getQuizEntriesByQuizId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable(name = "id") long id) {
        return quizService.getQuizById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("all-ids")
    public List<Long> getAllQuizzesIds() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        List<Long> ids = new ArrayList<>();
        for (Quiz quiz : quizzes) {
            ids.add(quiz.getId());
        }
        return ids;
    }


}