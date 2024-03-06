package com.unicorns.frontend;

import javafx.application.Application;
import javafx.stage.Stage;
import com.unicorns.frontend.controller.QuizAppController;

import java.io.IOException;

public class QuizzesApp extends Application  {
    private Stage primaryStage;

    private QuizAppController appController;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.appController = new QuizAppController(primaryStage);
        this.appController.initRootLayout();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
