package com.unicorns.frontend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresetEntryDTO {
    private String prizeContainerName;
    private int value;

}
