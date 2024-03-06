package com.unicorns.frontend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.Preset;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class StudentEditHandler {

    private  final String BACKEND_URL = Config.getProperty("api.url");
    private final String API_MODIFY_STUDENT = Config.getProperty("api.student_modify");

    public Long uploadPrize(Long prizeId, Long studentId, Long quizId) {
        try {
            String apiUrl = BACKEND_URL + API_MODIFY_STUDENT;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = String.format("{\"prizeId\": %d, \"studentId\": %d, \"quizId\": %d}", prizeId, studentId, quizId);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            return handleResponse(responseEntity);
        } catch (HttpClientErrorException e) {
            System.out.println("Wystąpił błąd HTTP: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            System.out.println("Wystąpił ogólny błąd: " + e.getMessage());
        }
        return null;
    }

    private Long handleResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Plik został pomyślnie przesłany."); //TODO: check
            Gson gson = new Gson();
//            Long presetId = gson.fromJson(responseEntity.getBody(), Long.class);
//            System.out.println("Quiz ID: " + presetId);
//            return presetId;
            return null;
        } else {
            System.out.println("Wystąpił problem podczas przesyłania pliku.");
            return null;
        }
    }

}
