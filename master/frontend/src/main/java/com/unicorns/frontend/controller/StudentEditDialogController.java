package com.unicorns.frontend.controller;

import com.unicorns.frontend.handler.PresetHandler;
import com.unicorns.frontend.handler.PrizeCategoryHandler;
import com.unicorns.frontend.handler.StudentEditHandler;
import com.unicorns.frontend.model.Prize;
import com.unicorns.frontend.model.QuizEntry;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Setter
public class StudentEditDialogController {
    @FXML
    public ComboBox prizesComboBox;
    @FXML
    public Button applyChangesButton;

    private Stage dialogStage;

    private QuizEntry quizEntry;
    private Long quizId;
    private StudentEditHandler studentEditHandler;

    public void setModel(QuizEntry quizEntry, Long quizId) {
        this.quizEntry = quizEntry;
        this.quizId = quizId;
        populateComboBox();
    }
    @FXML
    public void initialize() {
        applyChangesButton.disableProperty().bind(
                prizesComboBox.valueProperty().isNull()
        );
        studentEditHandler = new StudentEditHandler();

    }

    private void populateComboBox() {
        List<Prize> preferences = quizEntry.getPreferences();
        List<String> preferencesOrganized = IntStream.range(0, preferences.size())
                .mapToObj(i -> (i + 1) + ". " + preferences.get(i).getName())
                .toList();
        ObservableList<String> observablePreferences = FXCollections.observableList(preferencesOrganized);
        prizesComboBox.setItems(observablePreferences);
    }

    @FXML
    public void handleApplyChangesAction(ActionEvent actionEvent) {
        String selectedPrize = (String) prizesComboBox.getSelectionModel().getSelectedItem();
        int prizeNumber = Integer.parseInt(selectedPrize.substring(0, 1)); //tymczasowo
        Long prizeId = quizEntry.getPreferences().get(prizeNumber - 1).getId();
        studentEditHandler.uploadPrize(prizeId, quizEntry.getId(), quizId);
        dialogStage.close();
    }

}
