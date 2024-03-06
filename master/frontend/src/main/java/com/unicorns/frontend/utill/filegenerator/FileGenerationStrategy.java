package com.unicorns.frontend.utill.filegenerator;

import com.unicorns.frontend.model.Quiz;
import com.unicorns.frontend.model.QuizEntry;

import java.util.List;

public interface FileGenerationStrategy {

    void generateFile(List<QuizEntry> data, String path);

    public String getFileExtension();

    public String getFileName();




}
