package com.unicorns.frontend.utill.filegenerator;




import com.unicorns.frontend.model.QuizEntry;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class FileGenerator {

    private FileGenerationStrategy strategy;

    public void setGenerationStrategy(FileGenerationStrategy strategy) {
        this.strategy = strategy;
    }
    public void generateFile(List<QuizEntry> data) {
        String path = chooseFileLocation(strategy.getFileName(), strategy.getFileExtension());
        if (path != null) {
            strategy.generateFile(data,path);
        }
    }

    private String chooseFileLocation(String fileName, String fileExtension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
         fileChooser.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter(fileName, fileExtension)
         );

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

}
