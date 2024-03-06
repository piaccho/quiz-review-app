package com.unicorns.frontend.handler;

import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.QuizEntry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

public class QuizHandler {


    private final String BACKEND_URL = Config.getProperty("api.url");
    private  final String API_GET_QUIZZES_IDS =  Config.getProperty("api.get_all_quizzes_ids");
    private final String API_GET_QUIZ_BY_ID = Config.getProperty("api.get_quiz_by_id");

    public List<Long> fetchQuizzesIdsREST() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = BACKEND_URL + API_GET_QUIZZES_IDS;
            ResponseEntity<Long[]> response = restTemplate.getForEntity(apiUrl, Long[].class);
            return List.of(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas pobierania identyfikatorów quizów: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public  List<QuizEntry> fetchQuizEntriesREST(Long quizId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BACKEND_URL + API_GET_QUIZ_BY_ID;
        ResponseEntity<List<QuizEntry>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                quizId
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Quiz o id " + quizId + " został pomyślnie pobrany.");
//            System.out.println(response.getBody());
            return response.getBody();
        } else {
            System.out.println("Wystąpił problem podczas pobierania quizu.");
            return null;
        }
    }
}
