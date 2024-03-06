package com.unicorns.frontend.controller;

import com.unicorns.frontend.model.QuizEntry;
import com.unicorns.frontend.utill.filegenerator.FileGenerator;
import com.unicorns.frontend.utill.filegenerator.PdfGenerationStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import lombok.Setter;
import com.unicorns.frontend.utill.filegenerator.XlsxGenerationStrategy;
import java.util.List;

public class FileExportController {

    @FXML
    private RadioButton radioPDF;

    @FXML
    private RadioButton radioXLSX;

    @FXML
    private Button exportButton;

    @FXML
    private ToggleGroup formatToggleGroup;
    private List<QuizEntry> quizEntries;
    @Setter
    private Stage stage;


    public void setModel(List<QuizEntry> quizEntries)
    {
        this.quizEntries = quizEntries;
        exportButton.disableProperty().bind(formatToggleGroup.selectedToggleProperty().isNull());
    }

    @FXML
    private void handleExport(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) formatToggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String chosenFormat = selectedRadioButton.getText();
            FileGenerator generator = new FileGenerator();

            if ("PDF".equalsIgnoreCase(chosenFormat)) {
                generator.setGenerationStrategy(new PdfGenerationStrategy());
            }
            else if ("XLSX".equalsIgnoreCase(chosenFormat)) {
                generator.setGenerationStrategy(new XlsxGenerationStrategy());
            }

            generator.generateFile(quizEntries);
            stage.close();
        } else {

            System.out.println("Nie wybrano formatu.");
        }
    }

}
