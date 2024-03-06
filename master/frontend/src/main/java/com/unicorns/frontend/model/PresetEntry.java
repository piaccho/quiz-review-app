package com.unicorns.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresetEntry {
    private Long category;
    private int value;

}
