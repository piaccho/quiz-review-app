package com.unicorns.frontend.DTO;

import lombok.Data;

import com.unicorns.frontend.model.Strategy;
import java.util.List;
@Data
public class PresetDTO {
    private Long id;
    private String name;
    private Strategy strategy;
    private List<PresetEntryDTO> presetEntries;

}
