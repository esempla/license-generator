package com.esempla.lg.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RootLayoutController {

    private BorderPane borderPane;

    @FXML
    private void initialize() {
    }

    public void initRootLayout(Stage primaryStage) {
        try {
            borderPane = new FXMLLoader(getClass().getResource("/fxmls/rootLayout.fxml")).load();
            Scene scene = new Scene(borderPane);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
