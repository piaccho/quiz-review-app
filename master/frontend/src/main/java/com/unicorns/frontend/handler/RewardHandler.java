package com.unicorns.frontend.handler;

import com.unicorns.frontend.config.Config;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RewardHandler {
    private final String BACKEND_URL= Config.getProperty("api.url");
    private final String API_POST_APPLY_PRESET = Config.getProperty("api.post_apply_preset");

    public Long uploadPrizesToQuizREST(Long presetId, Long quizId) {
        try {


            String apiUrl = BACKEND_URL + API_POST_APPLY_PRESET.replace("{quizId}", String.valueOf(quizId))
                    .replace("{presetId}", String.valueOf(presetId));

//            String apiUrl = "http://localhost:8080/api/quizzes/results/1/1";
            HttpHeaders headers = new HttpHeaders();


            headers.setContentType(MediaType.APPLICATION_JSON);


            HttpEntity<String> requestEntity = new HttpEntity<>(headers);


            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            return handleResponse(responseEntity);
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP Error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());

        }

        return null;
    }

    private Long handleResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Prizes were successfully uploaded.");
        } else {
            System.out.println("Error during the POST request. Status code: " + responseEntity.getStatusCode());
        }
        return null;
    }
}
