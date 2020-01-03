package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import com.esempla.lg.model.Key;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.util.FXMLLoaderProvider;
import com.esempla.lg.util.FileSystemUtil;
import com.esempla.lg.util.KeyStorage;
import com.esempla.lg.util.ViewsFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MainController extends AbstractController{

    private KeyStorage keyStorage;
    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;

    @FXML
    private ListView<Key> keysListView;

    public MainController() {
        super(new FXMLLoaderProvider());
        log.info("constructor");
        this.keyStorage = KeyStorage.getInstance();
        this.fileSystemUtil = new FileSystemUtil();
        this.keyManager = new KeyManager();
    }

    @FXML
    private void initialize() {
        keyStorage.getKeys().setAll(fileSystemUtil.loadKeys());
        log.info("keys loaded");
        keysListView.setItems(keyStorage.getKeys());
    }

    @FXML
    void handleNewKey(ActionEvent event) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Key Generator");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        AnchorPane anchorPane = (AnchorPane) ViewsFactory.GEN_KEY_DIALOG.getNode(provider);

        GenKeyController controller = provider.getLoader().getController();
        controller.setStage(dialogStage);

        Scene scene = new Scene(anchorPane);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
