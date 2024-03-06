package com.unicorns.frontend.handler;

import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.Prize;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PrizeHandler {

    private final String BACKEND_URL = Config.getProperty("api.url");
    private final String API_GET_ALL_PRIZES = Config.getProperty("api.get_all_prizes");


    public List<Prize> fetchPrizesREST() {
        RestTemplate restTemplate = new RestTemplate();
        String url = BACKEND_URL + API_GET_ALL_PRIZES;
        ResponseEntity<List<Prize>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Prize>>() {}
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Wszystkie nagrody zostały pomyślnie pobrane.");
            System.out.println(response.getBody());
            System.out.println(response.getBody());
            return response.getBody();
        } else {
            System.out.println("Wystąpił problem podczas pobierania nagród.");
            return null;
        }
    }

}
