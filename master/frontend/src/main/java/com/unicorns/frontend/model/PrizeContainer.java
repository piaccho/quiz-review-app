package com.unicorns.frontend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
public class PrizeContainer {

    private List<Prize> prizes;



    public Optional<Prize> getPrizeById(Long id) {
        return prizes.stream()
                .filter(prize -> prize.getId().equals(id))
                .findFirst();
    }
    public Optional<Prize> getPrizeByPrizeName(String prizeName) {
        return prizes.stream()
                .filter(prize -> prize.getName().equals(prizeName))
                .findFirst();
    }
    public List<String> getPrizeNames() {
        return prizes.stream()
                .map(Prize::getName)
                .collect(Collectors.toList());
    }

}
