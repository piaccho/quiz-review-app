package com.unicorns.frontend.controller;

import com.unicorns.frontend.DTO.QuizQuestionDTO;
import com.unicorns.frontend.DTO.QuizStatisticsDTO;
import com.unicorns.frontend.Main;
import com.unicorns.frontend.model.QuizEntry;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

public class QuizAppController {

    private final String QUIZ_OVERVIEW_PANE_FXML = "view/QuizOverviewPane.fxml";
    public  final String PRESET_ADD_DIALOG_FXML = "view/PresetAddDialog.fxml";
    public  final String PRIZES_CUSTOMIZATION_DIALOG_FXML = "view/PrizesCustomizationDialog.fxml";
    public  final String STUDENT_EDIT_DIALOG_FXML = "view/StudentEditDialog.fxml";
    public  final String PRIZE_ADD_DIALOG_FXML = "view/PrizeAddDialog.fxml";

    public final String QUIZ_STATISTICS_DIALOG_FXML = "view/QuizStatisticsDialog.fxml";

    public  final String FILE_EXPORT_DIALOG_FXML = "view/ResultExportDialog.fxml";
    private Stage primaryStage;

    public QuizAppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("QuizPane");
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(QuizAppController.class.getClassLoader().getResource(QUIZ_OVERVIEW_PANE_FXML));
            BorderPane rootLayout = loader.load();

            QuizOverviewController controller = loader.getController();
            controller.setAppController(this);
            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    public void showDialog(String fxmlResource, String title, BiConsumer<Object, Stage> configurePresenter) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(QuizAppController.class.getClassLoader().getResource(fxmlResource));
        BorderPane layout = loader.load();

        Stage stage = new Stage();
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/unicorn.jpg")));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(new Scene(layout));

        configurePresenter.accept(loader.getController(), stage);

        stage.showAndWait();
    }

    public void showPresetAddDialog() throws IOException {
        showDialog(PRESET_ADD_DIALOG_FXML, "Add Preset", (presenter, stage) -> {
            PresetAddDialogController presetAddDialogController = (PresetAddDialogController) presenter;
            presetAddDialogController.setDialogStage(stage);
        });
    }

    public void showPrizesCustomizationDialog() throws IOException {
        showDialog(PRIZES_CUSTOMIZATION_DIALOG_FXML,"Customize Prizes", (presenter, stage) -> {
            // Additional configuration for Prizes Customization Dialog if needed
        });
    }

    public void showFileExportDialog(List<QuizEntry> quizEntries) throws IOException {
        showDialog(FILE_EXPORT_DIALOG_FXML, "Export", (presenter, stage) -> {
            FileExportController fileExportController = (FileExportController) presenter;
            fileExportController.setModel(quizEntries);
            fileExportController.setStage(stage);
        });
    }

    public void showEditStudentDialog(QuizEntry quizEntry, Long quizId) throws IOException {
        showDialog(STUDENT_EDIT_DIALOG_FXML, "Edit Student", (presenter, stage) -> {
            StudentEditDialogController studentEditDialogController = (StudentEditDialogController) presenter;
            studentEditDialogController.setModel(quizEntry, quizId);
            studentEditDialogController.setDialogStage(stage);
        });
    }

    public void showQuizStatisticsDialog(Long quizId) throws IOException{
        showDialog(QUIZ_STATISTICS_DIALOG_FXML, "Quiz " + quizId + " statistics", (presenter, stage) -> {
            QuizStatisticsDialogController quizStatisticsDialogController = (QuizStatisticsDialogController) presenter;
            quizStatisticsDialogController.setModel(quizId);
            quizStatisticsDialogController.setDialogStage(stage);
        });
    }
}
