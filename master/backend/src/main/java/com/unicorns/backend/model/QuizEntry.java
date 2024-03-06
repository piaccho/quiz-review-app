package com.unicorns.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_entry")
@JsonIgnoreProperties("quiz_entry")
public class QuizEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private int totalPoints;
    private long timeDifferenceInSeconds;
    private Date completingTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    private Prize prize;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Prize> preferences;
}