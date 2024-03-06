package com.unicorns.frontend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@Setter
public class Preset {
    private Long id;
    private String name;
    private Strategy strategy;
    private List<PresetEntry> presetEntries;

    public Preset(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
