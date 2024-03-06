package com.unicorns.frontend.controller;

import com.unicorns.frontend.DTO.PrizeCategoryDTO;
import com.unicorns.frontend.handler.PrizeCategoryHandler;
import com.unicorns.frontend.handler.PrizeHandler;
import com.unicorns.frontend.model.Prize;
import com.unicorns.frontend.model.PrizeCategory;
import com.unicorns.frontend.model.PrizeCategoryContainer;
import com.unicorns.frontend.model.PrizeContainer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PrizeCategoryAddController {

    @FXML
    public TextField categoryTextField;
    @FXML
    public TableView<String> prizesTableView;
    @FXML
    public TableColumn<String, String> prizeNameColumn;
    @FXML
    public Button addPrizeButton;
    @FXML
    public Label validationInfo;
    @FXML
    public ComboBox<String> prizesCategoryBox;
    @FXML
    public Button addPrizeBoxButton;
    private PrizeContainer prizeContainer;

    private HashMap<String, Long> chosenPrizesMap;

    private PrizeHandler prizeHandler;
    private PrizeCategoryHandler prizeCategoryHandler;

    private PrizeCategoryContainer prizeCategoryContainer;

    @Setter
    private Stage stage;


    public void initialize()  {
        addPrizeButton.disableProperty().bind(prizesCategoryBox.valueProperty().isNull());
        prizeNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        addPrizeBoxButton.disableProperty().bind(categoryTextField.textProperty().isEmpty());

    }
    public void setModel(PrizeCategoryContainer prizeCategoryContainer,PrizeContainer prizeContainer) {
        this.chosenPrizesMap = new HashMap<>();
        this.prizeHandler = new PrizeHandler();
        this.prizeCategoryHandler = new PrizeCategoryHandler();
        this.prizeCategoryContainer = prizeCategoryContainer;
        this.prizeContainer = prizeContainer;
        populateComboBox();
    }


    public void handleAddPrizeCategoryAction(ActionEvent actionEvent) {
        uploadPrizeCategory();
        addNewPrizeCategory();
    }

    private void uploadPrizeCategory() {
        PrizeCategoryDTO prizeCategoryDTO = new PrizeCategoryDTO();
        prizeCategoryDTO.setAvailablePrizes(new ArrayList<>(chosenPrizesMap.values()));
        prizeCategoryDTO.setName(categoryTextField.getText());
        prizeCategoryHandler.uploadPrizeCategoryREST(prizeCategoryDTO);
    }

    private void addNewPrizeCategory() {
        PrizeCategory prizeCategory = new PrizeCategory();
        prizeCategory.setName(categoryTextField.getText());
        List<Prize> prizeList =chosenPrizesMap.values().stream()
                .map(prizeId -> prizeContainer.getPrizeById(prizeId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        prizeCategory.setAvailablePrizes(prizeList);
        prizeCategoryContainer.getPrizes().add(prizeCategory);
        stage.close();
    }


    public void handleAddPrizeAction(ActionEvent actionEvent)
    {
        String prizeName = prizesCategoryBox.getValue();
        prizeContainer.getPrizeByPrizeName(prizeName).ifPresent(prize -> {
            prizesTableView.getItems().add(prizeName);
            chosenPrizesMap.put(prizeName, prize.getId());
            prizesCategoryBox.getSelectionModel().clearSelection();
        });
    }
    private void populateComboBox() {
        prizeContainer.setPrizes(prizeHandler.fetchPrizesREST());
        ObservableList<String> options = FXCollections.observableArrayList(prizeContainer.getPrizeNames());
        prizesCategoryBox.setItems(options);
    }



}
