package com.esempla.lg.controller;

import com.esempla.lg.model.Key;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.util.FileSystemUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import java.io.IOException;
@Slf4j
public class RootLayoutController {

    private BorderPane borderPane;
    private FileSystemUtil fileSystemUtil = new FileSystemUtil();
    private KeyManager keyManager = new KeyManager();


    @FXML
    private ListView<Key> keysListView;


    private ObservableList<Key> keys = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        keys.setAll(fileSystemUtil.loadKeys());
        log.info("keys loaded");
        keysListView.setItems(keys);
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


    public ObservableList<Key> getKeys() {
        return keys;
    }
}
