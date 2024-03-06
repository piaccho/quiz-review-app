package com.unicorns.frontend.handler;

import com.unicorns.frontend.DTO.QuizStatisticsDTO;
import com.unicorns.frontend.config.Config;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class QuizStatisticsHandler {
    private static final String BACKEND_URL = Config.getProperty("api.url");
    private static final String API_GET_QUIZ_STATISTICS_BY_QUIZ_ID =  Config.getProperty("api.get_quiz_statistics_by_quiz_id");

    public static QuizStatisticsDTO fetchQuizStatisticsREST(Long quizId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BACKEND_URL + API_GET_QUIZ_STATISTICS_BY_QUIZ_ID + quizId;
        System.out.println(url);
        ResponseEntity<QuizStatisticsDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Statystyki quizo o id " + quizId + " zostały pomyślnie pobrane.");
            System.out.println(response.getBody());
            return response.getBody();
        } else {
            System.out.println("Wystąpił problem podczas pobierania statystyk quizu.");
            return null;
        }
    }
}
