package com.unicorns.frontend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PrizeCategory {
    private Long id;

    private String name;

    private List<Prize> availablePrizes;
}
