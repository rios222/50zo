package com.cincuentazo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the JavaFX application and shows the first screen.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getResource("fxml/menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Cincuentazo");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private java.net.URL getResource(String path) {
        return getClass().getResource("/" + path);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
