package com.unicorns.frontend.model;

import com.unicorns.frontend.config.Config;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Quiz {
    private Long id;
    private LocalDateTime createdAt;
    private List<QuizEntry> quizEntries;






}
