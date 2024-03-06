package com.unicorns.backend.request;

import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PrizeCategoryRequest {
    private Long id;
    private String name;
    private List<Long> availablePrizes;
}
