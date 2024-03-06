package com.unicorns.backend.request;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PresetEntryRequest {
    private Long category;
    private int value;
}
