package com.unicorns.frontend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unicorns.frontend.DTO.PrizeCategoryDTO;
import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.PrizeCategory;
import com.unicorns.frontend.model.PrizeContainer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PrizeCategoryHandler {
    private final String BACKEND_URL= Config.getProperty("api.url");
    private final String API_POST_PRIZE_CONTAINER = Config.getProperty("api.post_prize_container");
    private static final String API_GET_PRIZE_CONTAINERS = Config.getProperty("api.get_all_prize_containers");
    public Long uploadPrizeCategoryREST(PrizeCategoryDTO prizeCategory) {
        if (prizeCategory != null) {
            try {
                return uploadPrizeCategoryToApi(prizeCategory);
            } catch (HttpClientErrorException e) {
                System.out.println("Wystąpił błąd HTTP: " + e.getStatusCode() + " - " + e.getStatusText());
            } catch (Exception e) {
                System.out.println("Wystąpił ogólny błąd: " + e.getMessage());
            }
        }
        return null;
    }

    private Long uploadPrizeCategoryToApi(PrizeCategoryDTO prizeCategory) {
        String apiUrl = BACKEND_URL + API_POST_PRIZE_CONTAINER;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = createRequestEntity(prizeCategory);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        return handleResponse(responseEntity);
    }

    private HttpEntity<String> createRequestEntity(PrizeCategoryDTO prizeCategory) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String prizeContainerJson = objectMapper.writeValueAsString(prizeCategory);
            System.out.println(prizeContainerJson);
            return new HttpEntity<>(prizeContainerJson, headers);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Long handleResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Plik został pomyślnie przesłany.");
            Gson gson = new Gson();
            Long prizeId = gson.fromJson(responseEntity.getBody(), Long.class);
            System.out.println("Prize ID: " + prizeId);
            return prizeId;
        } else {
            System.out.println("Wystąpił problem podczas przesyłania pliku.");
            return null;
        }
    }

    public  List<PrizeCategory> fetchAllPrizeCategoryREST()  {
        RestTemplate restTemplate = new RestTemplate();
        String url = BACKEND_URL + API_GET_PRIZE_CONTAINERS;
        ResponseEntity<List<PrizeCategory>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PrizeCategory>>() {}
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody().size() + " PrizeContainers pobranych z serwera.");
//            System.out.println(response.getBody());
            return response.getBody();
        } else {
            System.out.println("Wystąpił problem podczas pobierania quizu.");
            return null;
        }
    }
}
