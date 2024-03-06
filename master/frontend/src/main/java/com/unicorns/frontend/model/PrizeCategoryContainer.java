package com.unicorns.frontend.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
@Setter
@Getter
public class PrizeCategoryContainer {

    private final ObservableList<PrizeCategory> prizes = FXCollections.observableArrayList();

    public ObservableList<PrizeCategory> getPrizes() {
        return prizes;
    }

    public Optional<PrizeCategory> getPrizeCategoryByName(String name) {
        return prizes.stream()
                .filter(prizeCategory -> prizeCategory.getName().equals(name))
                .findFirst();
    }
}


