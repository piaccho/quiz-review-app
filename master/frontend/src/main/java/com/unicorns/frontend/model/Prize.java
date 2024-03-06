package com.unicorns.frontend.model;

import com.unicorns.frontend.config.Config;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Data
@Getter
@Setter
public class Prize {
    private Long id;
    private String name;
    private String description;

}
