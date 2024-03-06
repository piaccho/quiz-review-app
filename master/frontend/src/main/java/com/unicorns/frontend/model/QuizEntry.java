package com.unicorns.frontend.model;

import com.unicorns.frontend.config.Config;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
@Data
@Getter
@Setter
public class QuizEntry {
    private Long id;
    private String nickname;
    private int totalPoints;
    private long timeDifferenceInSeconds;
    private Date completingTime;
    private Prize prize;
    private List<Prize> preferences;



}
