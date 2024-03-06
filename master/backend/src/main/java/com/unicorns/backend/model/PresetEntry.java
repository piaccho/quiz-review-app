package com.unicorns.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "preset_entry")
@JsonIgnoreProperties("preset_entry")
public class PresetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`value`")
    private int value;

    @ManyToOne
    private PrizeCategory prizeCategory;
}
