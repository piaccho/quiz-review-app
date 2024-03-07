package com.unicorns.frontend.controller;

import com.unicorns.frontend.Main;
import com.unicorns.frontend.handler.PrizeCategoryHandler;
import com.unicorns.frontend.handler.PrizeHandler;
import com.unicorns.frontend.model.Prize;
import com.unicorns.frontend.model.PrizeCategory;
import com.unicorns.frontend.model.PrizeCategoryContainer;
import com.unicorns.frontend.model.PrizeContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PrizesCustomizationDialogController {

    public final String PRIZE_ADD_DIALOG_FXML = "view/PrizeAddDialog.fxml";

    public final String PRIZE_CONTAINER_ADD_DIALOG_FXML = "view/PrizeCategoryAddDialog.fxml";

    @FXML
    public TextField prizeTextField;
    @FXML
    public TextField descriptionPrizeTextField;
    @FXML
    public Button insertPrizeButton;
    @FXML
    public ListView<PrizeCategory> prizeBoxesListView;
    @FXML
    public ListView<Label> prizesListView;

    private PrizeHandler prizeHandler;

    private  Stage dialogStage;

    private PrizeCategoryContainer prizeCategoryContainer;

    private PrizeContainer prizeContainer;

    private PrizeCategoryHandler prizeCategoryHandler;

    public void initialize() {
        this.prizeHandler = new PrizeHandler();
        this.prizeCategoryContainer = new PrizeCategoryContainer();
        this.prizeContainer = new PrizeContainer();
        this.prizeCategoryHandler = new PrizeCategoryHandler();
        populatePrizeBoxesListView();
        renderPrizeDescriptionsList();

    }
    public void handleAddPrizeDialogAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(QuizAppController.class.getClassLoader().getResource(PRIZE_ADD_DIALOG_FXML));
        BorderPane rootLayout = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootLayout));
        stage.showAndWait();

    }

    public void handleAddPrizeCategoryDialogAction(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(QuizAppController.class.getClassLoader().getResource(PRIZE_CONTAINER_ADD_DIALOG_FXML));
        BorderPane rootLayout = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Add PrizeBox");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icons/unicorn.jpg")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(rootLayout));

        PrizeCategoryAddController presenter = loader.getController();
        presenter.setModel(prizeCategoryContainer,prizeContainer);
        presenter.setStage(stage);


        stage.showAndWait();

    }
    private void renderPrizeDescriptionsList() {
        prizeBoxesListView.getItems().clear();
        prizesListView.getItems().clear();
        prizeCategoryHandler.fetchAllPrizeCategoryREST().forEach(
                prizeCategory -> {
                    prizeCategoryContainer.getPrizes().add(prizeCategory);
                }
        );

        populatePrizeBoxesListView();
        addSelectionListenerToPrizeBoxesListView();
    }

    private void addSelectionListenerToPrizeBoxesListView() {
        prizeBoxesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String prizeCategoryName = newSelection.getName();
                prizeCategoryContainer.getPrizeCategoryByName(prizeCategoryName)
                        .ifPresent(prizeCategory -> prizeContainer.setPrizes(prizeCategory.getAvailablePrizes()));
                displayPrizes();
            }
        });
    }
    private void displayPrizes() {

        prizesListView.getItems().clear();
        List<Label> prizesLabels = new ArrayList<>();
        for (Prize prize : prizeContainer.getPrizes()) {
            Label label = new Label(prize.getName());
            prizesLabels.add(label);

        }
        prizesListView.getItems().addAll(prizesLabels);
    }

    private void populatePrizeBoxesListView() {
        prizeBoxesListView.setItems(prizeCategoryContainer.getPrizes());
        prizeBoxesListView.setCellFactory(lv -> new ListCell<PrizeCategory>() {
            @Override
            protected void updateItem(PrizeCategory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    public void handleEditPrizeBoxAction(ActionEvent actionEvent)
    {
        prizeBoxesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String prizeCategoryName = newSelection.getName();
                prizeCategoryContainer.getPrizeCategoryByName(prizeCategoryName)
                        .ifPresent(prizeCategory -> prizeContainer.setPrizes(prizeCategory.getAvailablePrizes()));
                displayPrizes();
            }
        });


    }
    public void handleAddPrizeAction(ActionEvent actionEvent)
    {


    }




}
