package com.unicorns.frontend.utill;

import com.google.gson.Gson;
import com.unicorns.frontend.config.Config;
import javafx.stage.FileChooser;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class FileUploader {
    private final String BACKEND_URL= Config.getProperty("api.url");
    private final String API_POST_QUIZ = Config.getProperty("api.post_quiz");

    public  Long uploadFile() {
        File file = chooseFile();
        if (file != null) {
            try {
                return uploadFileToApi(file);
            } catch (HttpClientErrorException e) {
                System.out.println("Wystąpił błąd HTTP: " + e.getStatusCode() + " - " + e.getStatusText());
            } catch (Exception e) {
                System.out.println("Wystąpił ogólny błąd: " + e.getMessage());
            }
        }
        return null;
    }

    private  File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Config.getProperty("file_upload.descriptions"), Config.getProperty("file_upload.extensions")));
        return fileChooser.showOpenDialog(null);
    }

    private Long uploadFileToApi(File file) throws RestClientException {
        String apiUrl = BACKEND_URL + API_POST_QUIZ;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = createRequestEntity(file);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        return handleResponse(responseEntity);
    }

    private HttpEntity<MultiValueMap<String, Object>> createRequestEntity(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        body.add("file", fileSystemResource);
        return new HttpEntity<>(body, headers);
    }

    private static Long handleResponse(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Plik został pomyślnie przesłany.");
            Gson gson = new Gson();
            Long quizId = gson.fromJson(responseEntity.getBody(), Long.class);
            System.out.println("Nadano ID quizu: " + quizId);
            return quizId;
        } else {
            System.out.println("Wystąpił problem podczas przesyłania pliku.");
            return null;
        }
    }
}
