package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import com.esempla.lg.model.Digest;
import com.esempla.lg.model.Key;
import com.esempla.lg.model.KeySize;
import com.esempla.lg.service.FilesManager;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.service.LicenseService;
import com.esempla.lg.util.FXMLLoaderProvider;
import com.esempla.lg.util.FileSystemUtil;
import com.esempla.lg.util.KeyStorage;
import com.esempla.lg.util.ViewsFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax0.license3j.License;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;

@Slf4j
public class MainController extends AbstractController{

    private KeyStorage keyStorage;
    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;
    private FilesManager filesManager;
    private LicenseService licenseService;

    @FXML
    private ListView<Key> keysListView;

    @FXML
    private TextArea licenseTextArea;

    @FXML
    private TextArea licenseEncTextArea;

    @FXML
    private ChoiceBox<Digest> digestChoiceBox;

    @FXML
    private Button signButton;

    @FXML
    private Button verifyButton;

    @FXML
    private Button saveButton;

    public MainController() {
        super(new FXMLLoaderProvider());
        log.info("constructor");
        this.keyStorage = KeyStorage.getInstance();
        this.fileSystemUtil = new FileSystemUtil();
        this.keyManager = new KeyManager();
        this.filesManager = new FilesManager();
        this.licenseService = new LicenseService();
    }

    @FXML
    private void initialize() {
        keyStorage.getKeys().setAll(fileSystemUtil.loadKeys());
        log.info("keys loaded");
        keysListView.setItems(keyStorage.getKeys());
        digestChoiceBox.setItems(FXCollections.observableArrayList(Digest.values()));
        binding();
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

    @FXML
    void handleUploadButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            log.info(file.getName().toString());
        }
        String data = filesManager.readFromFile(file);
        if (data != null){
            log.info("the "+file.getName()+" contains: \n"+ data);
            log.info("set text for licenseTextArea:");
            licenseTextArea.textProperty().set(data);
        }
        else{
            log.warn("The file: "+file.getName()+" is empty");
        }
    }

    @FXML
    void handleSaveButton(ActionEvent event) {

    }

    @FXML
    void handleSignButton(ActionEvent event) {
        PrivateKey privateKey = keysListView.getSelectionModel().getSelectedItem().getKeyPair().getPair().getPrivate();
        String digest = digestChoiceBox.getSelectionModel().getSelectedItem().value();
        License license = License.Create.from(licenseTextArea.getText());
        licenseService.signLicence(license,privateKey,digest);
        licenseEncTextArea.textProperty().setValue(String.valueOf(license.get("licenseSignature")));
        log.info("something happens");
    }

    @FXML
    void handleVerifyButton(ActionEvent event) {

    }


    void binding(){

                signButton.disableProperty().bind(
                        keysListView.getSelectionModel().selectedItemProperty().isNull()
                        .or(
                                licenseTextArea.textProperty().isEmpty())
                        .or(
                                digestChoiceBox.getSelectionModel().selectedItemProperty().isNull())
                        .or(
                               Bindings.createBooleanBinding(() -> !licenseService.isLicense(licenseTextArea.textProperty()),licenseTextArea.textProperty())
                        ));
    }



}
