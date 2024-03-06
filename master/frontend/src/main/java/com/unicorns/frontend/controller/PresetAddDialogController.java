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

    @FXML
    private Label maxPointsLabel;

    private int maxPoints;

    private Stage dialogStage;
    private int valueLimit;

    private Strategy addingPresetStrategy;
    private int presetEntriesNumber;

    private HashMap<String, Long> prizeContainerMap;
    private List<PresetEntry> presetEntries;

    private PrizeCategoryHandler prizeCategoryHandler;

    private PresetHandler presetHandler;

    private List<Long> usedValues;

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
                presetEntriesNumber = 0;

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
                presetEntriesNumber = 0;
                setTimeStrategy();

            }
        });
    }

    private int getMaxPointsFromQuiz() {
        return 2;
    }

    private void setTimeStrategy() {

        valueLimit = 0;
        maxPointsLabel.setText("Actual value: " + valueLimit);
    }

    private void setPointsStrategy() {

        usedValues = new ArrayList<>();
        valueLimit = 0;
        maxPointsLabel.setText("Max points: " + maxPoints);
    }

        public void handleAddPresetEntryButtonClick(ActionEvent actionEvent) {
        try {
            String prizeContainerName = prizeContainersComboBox.getValue();
            int value = Integer.parseInt(valueTextField.getText());
            if (validatePresetEntry(prizeContainerName, value)) {
                PresetEntry presetEntry = new PresetEntry(prizeContainerMap.get(prizeContainerName), value);
                if (presetEntries.contains(presetEntry)) {
                    showErrorLabel("Preset entry already exists!");
                    return;
                }
                PresetEntryDTO presetEntryDTO = new PresetEntryDTO(prizeContainerName, value);
                presetEntriesTableView.getItems().add(presetEntryDTO);
                presetEntries.add(presetEntry);
                presetEntriesNumber++;
                valueLimit += value;
                usedValues.add((long) value);
                prizeContainersComboBox.getSelectionModel().clearSelection();
                valueTextField.clear();
            }
        } catch (NumberFormatException e) {
            showErrorLabel("Value must be a number!");
        }
    }

    private boolean validatePresetEntry(String prizeContainerName, int value) {
        if (prizeContainerName != null && value >= 0) {
            System.out.println("Preset entries number: " + presetEntriesNumber);
            if(addingPresetStrategy == Strategy.TIME) {
                if (presetEntriesNumber >= 2) {
                    showErrorLabel("Time strategy can have only 2 entries");
                    return false;
                }
                if (presetEntriesNumber == 1 && valueLimit + value != 100) {
                    showErrorLabel("Sum of time entries must be equal to 100");
                    return false;
                }
                if (valueLimit + value > 100) {
                    showErrorLabel("Sum of time entries cannot be greater than 100!");
                    return false;
                }
                maxPointsLabel.setText("Actual value: " + value);
                return true;
            }
            if(addingPresetStrategy == Strategy.POINTS) {
                if (presetEntriesNumber > maxPoints) {
                    showErrorLabel("Cannot add more entries than possible points!");
                    return false;
                }
                if (value > maxPoints) {
                    showErrorLabel("Cannot add more points than possible points!");
                    return false;
                }
                if (usedValues.contains((long) value)) {
                    showErrorLabel("Value already used!");
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
