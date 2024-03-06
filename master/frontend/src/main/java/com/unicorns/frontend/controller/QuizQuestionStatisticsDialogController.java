package com.unicorns.frontend.controller;

import com.unicorns.frontend.DTO.QuizQuestionDTO;
import com.unicorns.frontend.DTO.QuizStatisticsDTO;
import com.unicorns.frontend.handler.QuizStatisticsHandler;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.util.Map;

@Setter
public class QuizQuestionStatisticsDialogController {
    @FXML
    public BarChart statisticsChart;

    @Setter
    private Stage stage;

    private Stage dialogStage;

    private QuizQuestionDTO quizQuestion;

    public void setModel(QuizQuestionDTO quizQuestion) {
        this.quizQuestion = quizQuestion;
        initializeBarChart();
    }

    private void initializeBarChart() {
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Double> entry : quizQuestion.getOptionsPercentages().entrySet()) {
            String option = entry.getKey();
            int percentageValue = (int) (entry.getValue() * 100);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(option);

            System.out.println(quizQuestion.getQuestionContent() + " - percentageValue = " + percentageValue);
            XYChart.Data<String, Number> data = new XYChart.Data<>("", percentageValue);
            series.getData().add(data);

            // Add a label to the data
            data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Label label = new Label(percentageValue + "%");
                    label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                    StackPane.setAlignment(label, Pos.TOP_CENTER);
                    ((StackPane) newValue).getChildren().add(label);
                }
            });

            barChartData.add(series);
        }

        statisticsChart.setData(barChartData);
    }

}
