package com.unicorns.frontend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.unicorns.frontend.DTO.PresetDTO;
import com.unicorns.frontend.config.Config;
import com.unicorns.frontend.model.Preset;
import com.unicorns.frontend.model.PresetDescriptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PresetHandler {
    private  final String BACKEND_URL = Config.getProperty("api.url");
    private  final String API_GET_PRESET_BY_ID = Config.getProperty("api.get_preset_by_id");
    private  final String API_GET_PRESETS_IDS =  Config.getProperty("api.get_all_presets_ids");
    private  final String API_POST_PRESET = Config.getProperty("api.post_preset");
    public  List<PresetDescriptor> fetchPresetsDescriptorsREST() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = BACKEND_URL + API_GET_PRESETS_IDS;
            ResponseEntity<List<PresetDescriptor>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PresetDescriptor>>() {}
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println(response.getBody().size() + " Preset descriptors pobrano z serwera.");
                return response.getBody();
            }
            else {
                System.out.println("Wystąpił problem podczas pobierania quizu.");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas pobierania deskryptorów presetów: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public  PresetDTO fetchPresetByIdREST(Long presetId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BACKEND_URL + API_GET_PRESET_BY_ID;
        ResponseEntity<PresetDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PresetDTO>() {},
                presetId
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Preset o id " + presetId + " został pomyślnie pobrany.");
            return response.getBody();
        } else {
            System.out.println("Wystąpił problem podczas pobierania presetu.");
            return null;
        }
    }

    public  Long uploadPreset(Preset preset) {
        if (preset != null) {
            try {
                return uploadPresetToApi(preset);
            } catch (HttpClientErrorException e) {
                System.out.println("Wystąpił błąd HTTP: " + e.getStatusCode() + " - " + e.getStatusText());
            } catch (Exception e) {
                System.out.println("Wystąpił ogólny błąd: " + e.getMessage());
            }
        }
        return null;
    }

    private  Long uploadPresetToApi(Preset preset) {
        String apiUrl = BACKEND_URL + API_POST_PRESET;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = createRequestEntity(preset);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        return handleResponse(responseEntity);
    }

    private  HttpEntity<String> createRequestEntity(Preset preset) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String presetJson = objectMapper.writeValueAsString(preset);
            System.out.println(presetJson);
            return new HttpEntity<>(presetJson, headers);
        } catch (Exception e) {
            // Handle the exception according to your needs
            e.printStackTrace();
            return null;
        }
    }

    private Long handleResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Plik został pomyślnie przesłany.");
            Gson gson = new Gson();
            Long presetId = gson.fromJson(responseEntity.getBody(), Long.class);
            System.out.println("Quiz ID: " + presetId);
            return presetId;
        } else {
            System.out.println("Wystąpił problem podczas przesyłania pliku.");
            return null;
        }
    }

}
