package com.cincuentazo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controls the menu screen where the user chooses the number of CPU players and starts the game.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class MenuController {
    @FXML
    private ComboBox<Integer> aiCountBox;

    @FXML
    public void initialize() {
        aiCountBox.getItems().addAll(1,2,3);
        aiCountBox.setValue(1);
    }

    @FXML
    public void onStart(ActionEvent event) throws IOException {
        int ai = aiCountBox.getValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
        Parent root = loader.load();
        GameController controller = loader.getController();
        controller.initGame(ai);
        Stage stage = (Stage) aiCountBox.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
