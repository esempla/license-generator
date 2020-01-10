package com.esempla.lg.controller;

import com.esempla.lg.model.EncryptAlghoritms;
import com.esempla.lg.model.IOFormatUsed;
import com.esempla.lg.model.Key;
import com.esempla.lg.model.KeySize;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.util.FXMLLoaderProvider;
import com.esempla.lg.util.FileSystemUtil;
import com.esempla.lg.util.KeyStorage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class GenKeyController extends AbstractController{

    @FXML
    private ChoiceBox<EncryptAlghoritms> algorithmChoiceBox;
    @FXML
    private ChoiceBox<KeySize> sizeChoiceBox;
    @FXML
    private ChoiceBox<IOFormat> formatChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField pathTextField;

    @FXML
    private Button generateButton;

    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;
    private KeyStorage keyStorage;

    public GenKeyController() {
        super(new FXMLLoaderProvider());
        this.fileSystemUtil = new FileSystemUtil();
        this.keyStorage = KeyStorage.getInstance();
        this.keyManager = new KeyManager();
    }

    @FXML
    private void initialize() {

        algorithmChoiceBox.setItems(FXCollections.observableArrayList(EncryptAlghoritms.values()));
        sizeChoiceBox.setItems(FXCollections.observableArrayList(KeySize.values()));
        formatChoiceBox.setItems(FXCollections.observableArrayList(IOFormatUsed.getMatch()));
        pathTextField.textProperty().set(FileSystemUtil.keysDirectoryPath);
        nameTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            pathTextField.textProperty().set(FileSystemUtil.keysDirectoryPath + File.separator + newValue);
        });
        binding();
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
        keyManager.writeKeyToFile(genKey, FileSystemUtil.keysDirectoryPath, formatChoiceBox.getSelectionModel().getSelectedItem());
        stage.close();
    }

    @FXML
    void cancelKeyPressed(ActionEvent event) {
        stage.close();
    }

    private void binding() {
        generateButton.disableProperty().bind(
                algorithmChoiceBox.getSelectionModel().selectedItemProperty().isNull()
                        .or(nameTextField.textProperty().isEmpty())
                        .or(formatChoiceBox.getSelectionModel().selectedItemProperty().isNull())
                        .or(sizeChoiceBox.getSelectionModel().selectedItemProperty().isNull()));

    }
}
