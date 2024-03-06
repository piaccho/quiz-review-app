package com.unicorns.backend.request;

import com.unicorns.backend.enums.Strategy;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PresetRequest {
    private String name;
    private Strategy strategy;
    private List<PresetEntryRequest> presetEntries;
}
