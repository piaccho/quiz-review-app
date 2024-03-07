package com.unicorns.frontend.controller;

import com.unicorns.frontend.DTO.QuizQuestionDTO;
import com.unicorns.frontend.DTO.QuizStatisticsDTO;
import com.unicorns.frontend.Main;
import com.unicorns.frontend.handler.QuizStatisticsHandler;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

@Setter
public class QuizStatisticsDialogController {
    public final String QUIZ_QUESTION_STATISTICS_DIALOG_FXML = "view/QuizQuestionStatisticsDialog.fxml";

    @FXML
    public BarChart statisticsChart;

    private Stage dialogStage;

    private Long quizId;
    private QuizStatisticsDTO quizStatistics;

    public void setModel(Long quizId) {
        this.quizId = quizId;
        Observable.fromCallable(this::getStatistics)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(quizStatisticsDTO -> {
                    this.quizStatistics = quizStatisticsDTO;
                    Platform.runLater(this::initializeBarChart);
                })
                .subscribe();
    }

    private QuizStatisticsDTO getStatistics() {
        return QuizStatisticsHandler.fetchQuizStatisticsREST(quizId);
    }

    @FXML
    public void initialize() {
        statisticsChart.lookupAll(".default-color0.chart-bar").forEach(node -> {
            node.setStyle("-fx-pref-width: 50;");
        });
    }

    private void initializeBarChart() {
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();

        for (QuizQuestionDTO quizQuestion : quizStatistics.getQuizQuestions()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(quizQuestion.getQuestionContent());

            int percentageValue = quizQuestion.getQuestionCorrectAnswers() * 100 / (quizQuestion.getQuestionCorrectAnswers() + quizQuestion.getQuestionWrongAnswers());

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

            data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    newValue.setOnMouseClicked(event -> {
                        try {
                            handleClickSeriesAction(quizQuestion);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
            barChartData.add(series);
        }
        statisticsChart.setData(barChartData);
    }

    @FXML
    public void handleClickSeriesAction(QuizQuestionDTO quizQuestion) throws IOException {
        System.out.println("Clicked on series: " + quizQuestion.getQuestionContent());
//        appController.showQuizQuestionStatisticsDialog(quizQuestion);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(QuizAppController.class.getClassLoader().getResource(QUIZ_QUESTION_STATISTICS_DIALOG_FXML));
        BorderPane rootLayout = loader.load();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/unicorn.jpg")));
        stage.setTitle(quizQuestion.getQuestionContent());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootLayout));

        QuizQuestionStatisticsDialogController presenter = loader.getController();
        presenter.setModel(quizQuestion);
        presenter.setStage(stage);

        stage.showAndWait();
    }
}
