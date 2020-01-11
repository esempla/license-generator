package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import com.esempla.lg.model.Digest;
import com.esempla.lg.model.IOFormatUsed;
import com.esempla.lg.model.Key;
import com.esempla.lg.model.KeySize;
import com.esempla.lg.service.FilesManager;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.service.LicenseService;
import com.esempla.lg.util.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.Border;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax0.license3j.License;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.esempla.lg.util.Anim.trueColor;

@Slf4j
public class MainController extends AbstractController{

    private KeyStorage keyStorage;
    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;
    private FilesManager filesManager;
    private LicenseService licenseService;
   private  License license;

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
        license = null;
    }

    @FXML
    private void initialize() {
//        staff();
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
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter txtExtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", ".txt");
        FileChooser.ExtensionFilter binExtFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", ".bin");
        FileChooser.ExtensionFilter base64ExtFilter = new FileChooser.ExtensionFilter("B64 files (*.base64)", ".base64");
        fileChooser.getExtensionFilters().addAll(txtExtFilter,binExtFilter,base64ExtFilter);
        File file = fileChooser.showSaveDialog(stage);


        if (file != null) {

            String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
            File fileWithExtension = new File(file.getPath()+extension);
            log.info("save licence to file: "+fileWithExtension.getPath());
            licenseService.writeLicenceToFile(license,fileWithExtension, filesManager.getExtension(fileWithExtension));
        }

    }

    @FXML
    void handleSignButton(ActionEvent event) {
        PrivateKey privateKey = keysListView.getSelectionModel().getSelectedItem().getKeyPair().getPair().getPrivate();
        String digest = digestChoiceBox.getSelectionModel().getSelectedItem().value();
       license = License.Create.from(licenseTextArea.getText());
        licenseService.signLicence(license,privateKey,digest);
        licenseEncTextArea.textProperty().setValue(Base64.getEncoder().encodeToString(license.serialized()));
        log.info("sign happens");
    }

    @FXML
    void handleVerifyButton(ActionEvent event) {
        PublicKey publicKey = keysListView.getSelectionModel().getSelectedItem().getKeyPair().getPair().getPublic();
        InputStream inputStream = new ByteArrayInputStream(licenseEncTextArea.getText().getBytes(StandardCharsets.UTF_8));

        License myLicense = licenseService.readLicenceFromStream(inputStream);
        log.info(String.valueOf(myLicense.isOK(publicKey)));
        blink();

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

                saveButton.disableProperty().bind(
                        licenseEncTextArea.textProperty().isEmpty()
                );

                verifyButton.disableProperty().bind(
                        licenseEncTextArea.textProperty().isEmpty()
                        .or(
                                keysListView.getSelectionModel().selectedItemProperty().isNull())
                );
    }

    void blink(){

        log.info("blinking");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> licenseEncTextArea.setStyle("text-area-background: "+ format(trueColor) +";")));
        timeline.setCycleCount(2);
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            licenseEncTextArea.setStyle("text-area-background: "+ format(Anim.defaultColor) +";");
        });
    }

    private String format(Color c) {
        int r = (int) (c.getRed());
        int g = (int) (c.getGreen());
        int b = (int) (c.getBlue());
        return String.format("#%02x%02x%02x", r, g, b);
    }


    void staff(){


    }
}
