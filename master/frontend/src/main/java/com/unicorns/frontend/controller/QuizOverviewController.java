package com.unicorns.frontend.controller;

// com.unicorns.frontend.handler.*;
//import com.unicorns.frontend.model.PresetDescriptor;
import com.unicorns.frontend.DTO.PresetDTO;
import com.unicorns.frontend.handler.PresetHandler;
import com.unicorns.frontend.handler.QuizHandler;
import com.unicorns.frontend.handler.RewardHandler;
import com.unicorns.frontend.model.PresetDescriptor;
import com.unicorns.frontend.model.Prize;
import com.unicorns.frontend.model.Quiz;
import com.unicorns.frontend.model.QuizEntry;
import com.unicorns.frontend.utill.FileUploader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.text.Font;
import lombok.Setter;
//import com.unicorns.frontend.DTO.PresetDTO;

public class QuizOverviewController {

    // BUTTONS
    @FXML
    public Button addPresetButton;
    @FXML
    public Button editStudentButton;
    @FXML
    public Button addQuizButton;
    @FXML
    public Button exportButton;
    @FXML
    public Button applyPresetButton;
    @FXML
    public Button editPresetButton;
    @FXML
    public Button customizePrizesButton;
    @FXML
    public Button statisticsButton;

    // LISTS
    @FXML
    public ListView<Label> presetListView;
    @FXML
    public ListView<Label> quizListView;
    @FXML
    public TableView<QuizEntry> quizTableView;
    @FXML
    public TableColumn<QuizEntry, String> nicknameColumn;
    @FXML
    public TableColumn<QuizEntry, Integer> totalPointsColumn;
    @FXML
    public TableColumn<QuizEntry, Integer> timeDifferenceInSecondsColumn;
    @FXML
    public TableColumn<QuizEntry, String> prizeColumn;

    private QuizEntry quizEntry;
    private QuizHandler quizHandler;
    private Quiz quiz;

    @Setter
    private QuizAppController appController;

    private List<Long> quizDescriptors;

    private FileUploader fileUploader;

    private List<PresetDescriptor> presetDescriptors;

    private PresetHandler presetHandler;

    private HashMap<String, Long> presetDescriptorMap;

    private PresetDTO currentPreset;

    private RewardHandler rewardHandler;

    @FXML
    public Label currentPresetName;

    public void initialize() {
        setModel();
        renderQuizDescriptionsList();
        renderPresetDescriptionsList();
        initializeButton();
    }

    public void renderQuizDescriptionsList() {
        quizListView.getItems().clear();
        quizDescriptors = quizHandler.fetchQuizzesIdsREST();
        populateQuizListView();
        setCellValueFactories();
        addSelectionListenerToQuizListView();
    }
    private void setModel() {
        this.quizEntry = new QuizEntry();
        this.fileUploader = new FileUploader();
        this.quiz = new Quiz();
        this.quizHandler = new QuizHandler();
        this.presetHandler = new PresetHandler();
        this.rewardHandler = new RewardHandler();
    }

    private void initializeButton() {
        exportButton.disableProperty().bind(Bindings.size(quizListView.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
        statisticsButton.disableProperty().bind(Bindings.size(quizListView.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
        applyPresetButton.disableProperty().bind(
                Bindings.size(presetListView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
                        .or(Bindings.size(quizListView.getSelectionModel().getSelectedItems()).isNotEqualTo(1))
        );
//        editPresetButton.disableProperty().bind(Bindings.size(presetListView.getSelectionModel()
//                .getSelectedItems()).isNotEqualTo(1));
        editStudentButton.disableProperty().bind(Bindings.size(quizTableView.getSelectionModel()
                .getSelectedItems()).isNotEqualTo(1));
        quizTableView.setPlaceholder(new Label("Select a quiz from the list to view its details."));
    }

    private void addSelectionListenerToQuizListView() {
        quizListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                long quizId = Long.parseLong(newSelection.getText().split(" ")[1]);
                System.out.println("Wybrano quiz o id: " + quizId);
                quiz.setQuizEntries(quizHandler.fetchQuizEntriesREST(quizId));
                quiz.setId(quizId);
                if (quiz.getQuizEntries() != null) {
                    displayQuizDetails();
                } else {
                    System.err.println("Nie udało się pobrać danych quizu o id: " + quizId);
                }
            }
        });
    }

    private void addSelectionListenerToPresetListView() {
        presetListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String presetName = newSelection.getText();
                Long presetId = presetDescriptorMap.get(presetName);
                System.out.println("Wybrano preset o id: " + presetId);
                currentPresetName.setText(Objects.requireNonNullElse(presetName, "Choose preset from list"));
                currentPreset = presetHandler.fetchPresetByIdREST(presetId);
            }
        });
    }
    private void populateQuizListView() {
        List<Label> quizLabels = new ArrayList<>();
        if (quizDescriptors != null) {
            for (Long desc : quizDescriptors) {
                Label label = new Label("Quiz " + desc);
                quizLabels.add(label);
            }
            quizListView.getItems().addAll(quizLabels);
        }
    }
    public void displayQuizDetails() {
        quizTableView.getItems().clear();
        ObservableList<QuizEntry> data = FXCollections.observableArrayList(quiz.getQuizEntries());
        quizTableView.setItems(data);


        prizeColumn.setCellFactory(column -> {
            return new TableCell<QuizEntry, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    if (!isEmpty()) {
                        if (item.equals("")) {
                            setStyle("-fx-background-color:transparent;");
                        } else {
                            setStyle("-fx-background-color:#FFD9FB;");
                        }
                    }
                }
            };
        });



        // sortowanie po punktach
        quizTableView.getSortOrder().add(totalPointsColumn);
        totalPointsColumn.setSortType(TableColumn.SortType.DESCENDING);
        totalPointsColumn.setSortable(true);
        quizTableView.sort();
    }
    private void setCellValueFactories() {
        nicknameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNickname()));
        totalPointsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalPoints()).asObject());
        timeDifferenceInSecondsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((int) cellData.getValue().getTimeDifferenceInSeconds()).asObject());
        prizeColumn.setCellValueFactory(cellData -> {
            Prize prize = cellData.getValue().getPrize();
            String prizeName = Optional.ofNullable(prize)
                    .map(Prize::getName)
                    .orElse("");
            return new SimpleStringProperty(prizeName);
        });
    }

    @FXML
    public void handleAddPresetAction(ActionEvent actionEvent) throws IOException {

        appController.showPresetAddDialog();
        renderPresetDescriptionsList();
    }

    @FXML
    public void handleAddQuizAction(ActionEvent actionEvent) {
        Long quizDescriptor = fileUploader.uploadFile();
        if (quizDescriptor != null) {
            quizListView.getItems().add(new Label("Quiz " + quizDescriptor));
        }
    }

    @FXML
    public void handlePrizesCustomizationAction(ActionEvent actionEvent) throws IOException {
        appController.showPrizesCustomizationDialog();
    }
    public void renderPresetDescriptionsList() {
        presetListView.getItems().clear();
        presetDescriptors = presetHandler.fetchPresetsDescriptorsREST();
        presetDescriptorMap = new HashMap<>();
        if (presetDescriptors != null) {
            for (PresetDescriptor desc : presetDescriptors) {
                presetDescriptorMap.put(desc.getName(), desc.getId());
            }
            populatePresetListView();
            addSelectionListenerToPresetListView();
        }


    }
    private void populatePresetListView() {
        List<Label> presetLabel = new ArrayList<>();
        if (presetDescriptors != null) {
            for (PresetDescriptor desc : presetDescriptors) {
                Label label = new Label(desc.getName());
                presetLabel.add(label);
            }
            presetListView.getItems().addAll(presetLabel);
        }
    }

    public void handleEditStudentAction(ActionEvent actionEvent) throws IOException {
        QuizEntry studentEntry = quizTableView.getSelectionModel().getSelectedItem();
        appController.showEditStudentDialog(studentEntry, quiz.getId());
        List<QuizEntry> currentQuizEntries = quizHandler.fetchQuizEntriesREST(quiz.getId());
        quiz.setQuizEntries(currentQuizEntries);
        quiz.setId(quiz.getId());
        if (currentQuizEntries != null) {

            displayQuizDetails();
        } else {
            System.err.println("Nie udało się pobrać danych quizu o id: " + quiz.getId());
        }
    }

//PROBLEM!!! DO NAPRAWY!!!
    public void handleApplyPresetAction(ActionEvent actionEvent) {
        try {
            Label selectedPresetItem = presetListView.getSelectionModel().getSelectedItem();
            Label selectedQuizItem = quizListView.getSelectionModel().getSelectedItem();
            if (selectedPresetItem != null) {
                String presetName = selectedPresetItem.getText();
                long presetId = presetDescriptorMap.get(presetName);
                if (selectedQuizItem != null) {
                    long quizId = Long.parseLong(selectedQuizItem.getText().split(" ")[1]);

                    rewardHandler.uploadPrizesToQuizREST(presetId, quizId);
                    List<QuizEntry> currentQuizEntries = quizHandler.fetchQuizEntriesREST(quizId);
                    quiz.setQuizEntries(currentQuizEntries);
                    quiz.setId(quizId);
                    if (currentQuizEntries != null) {

                        displayQuizDetails();
                    } else {
                        System.err.println("Nie udało się pobrać danych quizu o id: " + quizId);
                    }
                } else {
                    System.out.println("No quiz selected");
                }
            } else {
                System.out.println("No preset selected");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tu tez poprawa!!!

    public void handleExportQuizAction(ActionEvent actionEvent) throws IOException {
        Label selectedQuizItem = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuizItem != null) {
            long quizId = Long.parseLong(selectedQuizItem.getText().split(" ")[1]);
            List<QuizEntry> currentQuizEntries = quizHandler.fetchQuizEntriesREST(quizId);
            appController.showFileExportDialog(currentQuizEntries);


        }
    }
    @FXML
    public void handleStatisticsAction(ActionEvent actionEvent) throws IOException{
        appController.showQuizStatisticsDialog(quiz.getId());
    }
}

