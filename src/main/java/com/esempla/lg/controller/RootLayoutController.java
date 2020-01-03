package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import com.esempla.lg.model.Key;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.util.FileSystemUtil;
import com.esempla.lg.util.KeyStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RootLayoutController{

    private KeyStorage keyStorage;
    private BorderPane borderPane;
    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;
    private Stage primaryStage = Launcher.getPrimaryStage();


    public RootLayoutController() {
        log.info("constructor");
        this.keyStorage = KeyStorage.getInstance();
        this.fileSystemUtil = new FileSystemUtil();
        this.keyManager = new KeyManager();
    }

    @FXML
    private ListView<Key> keysListView;


    @FXML
    private void initialize() {
        keyStorage.getKeys().setAll(fileSystemUtil.loadKeys());
        log.info("keys loaded");
        keysListView.setItems(keyStorage.getKeys());
    }

    public void initRootLayout() {
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


    @FXML
    void handleNewKey(ActionEvent event) {

        GenKeyPanelController genKeyPanelController = new GenKeyPanelController();
        genKeyPanelController.showGenKeyDialog(this.primaryStage);

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
