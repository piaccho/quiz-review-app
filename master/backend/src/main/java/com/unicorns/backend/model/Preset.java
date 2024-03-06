package com.unicorns.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unicorns.backend.enums.Strategy;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "preset")
@JsonIgnoreProperties("preset")
public class Preset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Strategy strategy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PresetEntry> presetEntries = new ArrayList<>();
}
