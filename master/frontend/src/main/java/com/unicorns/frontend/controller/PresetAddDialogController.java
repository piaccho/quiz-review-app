package com.unicorns.frontend.controller;

import com.unicorns.frontend.DTO.PresetEntryDTO;
import com.unicorns.frontend.handler.PresetHandler;
import com.unicorns.frontend.handler.PrizeCategoryHandler;
import com.unicorns.frontend.handler.PrizeHandler;
import com.unicorns.frontend.model.Preset;
import com.unicorns.frontend.model.PresetEntry;
import com.unicorns.frontend.model.Strategy;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
public class PresetAddDialogController {
    @FXML
    public TextField nameTextField;
    @FXML
    public RadioButton typePointsButton;
    @FXML
    public RadioButton typeTimeButton;
    @FXML
    public TextField valueTextField;
    @FXML
    public ComboBox<String> prizeContainersComboBox;
    @FXML
    public Button addPresetEntryButton;
    @FXML
    public Button addPresetButton;
    @FXML
    public TableView<PresetEntryDTO> presetEntriesTableView;
    @FXML
    public TableColumn<PresetEntryDTO, String> prizeContainerNameColumn;
    @FXML
    public TableColumn<PresetEntryDTO, Integer> valueColumn;
    @FXML
    public Label validationInfo;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    public ListView prizesListView;

    private Stage dialogStage;
    private int valueLimit;

    private Strategy addingPresetStrategy;
    private int presetEntriesNumber;
    private int maxPoints;
    private HashMap<String, Long> prizeContainerMap;
    private List<PresetEntry> presetEntries;

    private PrizeCategoryHandler prizeCategoryHandler;

    private PresetHandler presetHandler;

    @FXML
    public void initialize() {
        addPresetEntryButton.setDisable(true);
        prizeContainerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrizeContainerName()));
        valueColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());
        this.presetEntriesNumber = 0;
        this.toggleGroup = new ToggleGroup();
        typePointsButton.setToggleGroup(toggleGroup);
        typeTimeButton.setToggleGroup(toggleGroup);
        this.presetEntries = new ArrayList<>();
        this.prizeCategoryHandler = new PrizeCategoryHandler();
        presetHandler = new PresetHandler();
        populateComboBox();
        addSelectionListenerToPrizeContainerComboBox();
        this.maxPoints = getMaxPointsFromQuiz();

    }

    private void populateComboBox() {
        prizeContainerMap = new HashMap<>();
        List<String> prizeContainersLabels = new ArrayList<>();
        prizeCategoryHandler.fetchAllPrizeCategoryREST().forEach(prizeContainer -> {
            prizeContainersLabels.add(prizeContainer.getName());
            prizeContainerMap.put(prizeContainer.getName(), prizeContainer.getId());
        });
        ObservableList<String> options = FXCollections.observableArrayList(prizeContainersLabels);
        prizeContainersComboBox.setItems(options);
    }

    private void addSelectionListenerToPrizeContainerComboBox() {
        typePointsButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                presetEntriesTableView.getItems().clear();
                presetEntries.clear();
                addPresetEntryButton.setDisable(false);
                setAddingPresetStrategy(Strategy.POINTS);
                presetEntries = new ArrayList<>();

                setPointsStrategy();
            }
        });

        typeTimeButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                presetEntriesTableView.getItems().clear();
                presetEntries.clear();
                addPresetEntryButton.setDisable(false);
                setAddingPresetStrategy(Strategy.TIME);
                presetEntries = new ArrayList<>();
                setTimeStrategy();

            }
        });
    }

    private int getMaxPointsFromQuiz() {
        //TODO: implement
        return 2;
    }

    private void setTimeStrategy() {
        valueLimit = 0;
    }

    private void setPointsStrategy() {
        valueLimit = 0;
    }

        public void handleAddPresetEntryButtonClick(ActionEvent actionEvent) {
        try {
            String prizeContainerName = prizeContainersComboBox.getValue();
            int value = Integer.parseInt(valueTextField.getText());
            if (validatePresetEntry(prizeContainerName, value)) {
                PresetEntryDTO presetEntry = new PresetEntryDTO(prizeContainerName, value);
                presetEntriesTableView.getItems().add(presetEntry);
                presetEntries.add(new PresetEntry(prizeContainerMap.get(prizeContainerName), value));
                presetEntriesNumber++;
                valueLimit += value;
                prizeContainersComboBox.getSelectionModel().clearSelection();
                valueTextField.clear();
            }
        } catch (NumberFormatException e) {
            showErrorLabel("Puste pola!");
        }
    }

    private boolean validatePresetEntry(String prizeContainerName, int value) {
        if (prizeContainerName != null && value >= 0) {
            if(addingPresetStrategy == Strategy.TIME) {
                if (presetEntriesNumber >= 2) {
                    showErrorLabel("Nie można dodać więcej niż 2 wpisów dla strategii czasowej!");
                    return false;
                }
                if (presetEntriesNumber == 1 && valueLimit + value != 100) {
                    showErrorLabel("Suma wartości dwóch wpisów musi wynosić 100");
                    return false;
                }
                if (valueLimit + value > 100) {
                    showErrorLabel("Suma wartości wpisów nie może przekraczać 100");
                    return false;
                }
                return true;
            }
            if(addingPresetStrategy == Strategy.POINTS) {
                if (presetEntriesNumber > maxPoints) {
                    showErrorLabel("Nie można dodać więcej wpisów niż możliwych punktów!");
                    return false;
                }
                if (value > maxPoints) {
                    showErrorLabel("Wartość wpisu nie może być większa niż możliwych punktów!");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public void handleAddPresetButtonClick(ActionEvent actionEvent) {
        Preset preset = new Preset();
        preset.setPresetEntries(presetEntries);
        preset.setStrategy(addingPresetStrategy);
        preset.setName(nameTextField.getText());
        presetHandler.uploadPreset(preset);
        dialogStage.close();
    }

    private void showErrorLabel(String errorMessage) {
        validationInfo.setText(errorMessage);
        validationInfo.setVisible(true);
        validationInfo.setTextFill(Color.RED);
    }



}
