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
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.NoSuchAlgorithmException;

public class GenKeyController extends AbstractController {

    final static Logger log = LoggerFactory.getLogger(GenKeyController.class);

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

    private KeyManager keyManager;
    private KeyStorage keyStorage;

    public GenKeyController() {
        super(new FXMLLoaderProvider());
        this.keyStorage = KeyStorage.getInstance();
        this.keyManager = new KeyManager();
    }

    @FXML
    private void initialize() {
        algorithmChoiceBox.setItems(FXCollections.observableArrayList(EncryptAlghoritms.values()));
        algorithmChoiceBox.setValue(EncryptAlghoritms.RSA);

        sizeChoiceBox.setItems(FXCollections.observableArrayList(KeySize.values()));
        sizeChoiceBox.setValue(KeySize.S2048);

        formatChoiceBox.setItems(FXCollections.observableArrayList(IOFormatUsed.getMatch()));
        formatChoiceBox.setValue(IOFormat.BASE64);

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
            log.error("Error on generate key : {}", e.getMessage());
        }
        log.info(algorithmChoiceBox.getSelectionModel().getSelectedItem().value());
        keyStorage.addKey(genKey);
        keyManager.writeKeyToFile(genKey, FileSystemUtil.keysDirectoryPath,
                formatChoiceBox.getSelectionModel().getSelectedItem());
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
