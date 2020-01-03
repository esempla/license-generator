package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import com.esempla.lg.model.EncryptAlghoritms;
import com.esempla.lg.model.Key;
import com.esempla.lg.model.KeySize;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.util.FileSystemUtil;
import com.esempla.lg.util.KeyStorage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax0.license3j.crypto.LicenseKeyPair;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class GenKeyPanelController {
    KeyStorage keyStorage;

    private Stage dialogStage;
    private Stage primaryStage = Launcher.getPrimaryStage();

    @FXML
    ChoiceBox<EncryptAlghoritms> algorithmChoiceBox;

    @FXML
    private ChoiceBox<KeySize> sizeChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField pathTextField;

    @FXML
    private Button generateButton;


    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;

    public GenKeyPanelController() {
        this.fileSystemUtil = new FileSystemUtil();
        this.keyStorage = KeyStorage.getInstance();
        this.keyManager = new KeyManager();
        this.dialogStage = new Stage();
        log.info("constructor 2");
    }

    @FXML
    private void initialize() {

        algorithmChoiceBox.setItems(FXCollections.observableArrayList(EncryptAlghoritms.values()));
        sizeChoiceBox.setItems(FXCollections.observableArrayList(KeySize.values()));
        pathTextField.textProperty().set(FileSystemUtil.keysDirectoryPath);
        nameTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            pathTextField.textProperty().set(FileSystemUtil.keysDirectoryPath + File.separator + newValue);
        });
        binding();
    }

    public void showGenKeyDialog(Stage primaryStage) {
        try {
            // Create the dialog Stage.

            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/genKeyPanel.fxml"));
            AnchorPane page =  loader.load();
            this.primaryStage = primaryStage;

            this.dialogStage.setTitle("Key Generator");
            this.dialogStage.initModality(Modality.WINDOW_MODAL);
            this.dialogStage.initOwner( this.primaryStage);


            Scene scene = new Scene(page);
            this.dialogStage.setScene(scene);

            // Show the dialog and wait until the user closes it
            this.dialogStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    void generateKeyPressed(ActionEvent event) {
        Key genKey = new Key();
        genKey.setName(nameTextField.textProperty().getValue());
        try {
            genKey.setKeyPair(
                    LicenseKeyPair.Create.from(
                            algorithmChoiceBox.getSelectionModel().getSelectedItem().value(),
                            sizeChoiceBox.getSelectionModel().getSelectedItem().value()
                    )
            );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info(algorithmChoiceBox.getSelectionModel().getSelectedItem().value());
        keyStorage.addKey(genKey);
        keyManager.writeKeyToFile(genKey, FileSystemUtil.keysDirectoryPath);
        this.dialogStage.close();
    }

    @FXML
    void cancelKeyPressed(ActionEvent event) {

        log.info("LLLLLLLLLLLLLLLLLLLLLLLLLLLLL");


        System.err.println("Launcher.getPrimaryStage() >>>>" +  this.primaryStage.toString());

        this.dialogStage.close();
    }

    private void binding(){
        generateButton.disableProperty().bind(
                algorithmChoiceBox.getSelectionModel().selectedItemProperty().isNull()
                        .or(nameTextField.textProperty().isEmpty()
                                .or(sizeChoiceBox.getSelectionModel().selectedItemProperty().isNull())));

    }


}
