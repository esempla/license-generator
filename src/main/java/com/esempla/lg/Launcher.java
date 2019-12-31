package com.esempla.lg;

import com.esempla.lg.controller.FrontPanelController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class Launcher extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("License generator");
        log.info("bravo");
        initRootLayout();
        loadFrontPanel();


    }



    private void initRootLayout() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Launcher.class.getResource("/fxmls/rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFrontPanel() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Launcher.class.getResource("/fxmls/frontPanel.fxml"));
            AnchorPane frontPanel = (AnchorPane) loader.load();

            rootLayout.setCenter(frontPanel);

            FrontPanelController controller = loader.getController();
            controller.setMainApp(this);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
