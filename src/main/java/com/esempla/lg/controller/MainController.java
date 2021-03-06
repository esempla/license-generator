package com.esempla.lg.controller;


import com.esempla.lg.model.Digest;
import com.esempla.lg.model.Key;
import com.esempla.lg.service.FilesManager;
import com.esempla.lg.service.KeyManager;
import com.esempla.lg.service.LicenseService;
import com.esempla.lg.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax0.license3j.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;

public class MainController extends AbstractController {

    final static Logger log = LoggerFactory.getLogger(MainController.class);

    private KeyStorage keyStorage;
    private FileSystemUtil fileSystemUtil;
    private KeyManager keyManager;
    private FilesManager filesManager;
    private LicenseService licenseService;
    private License license;

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

    @FXML
    private MenuItem deleteKeyMenuItem;

    @FXML
    private MenuItem showDumpPublicKeyMItem;

    @FXML
    private MenuItem saveDumpPublicKeyMItem;

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
        keyStorage.getKeys().setAll(fileSystemUtil.loadKeys());
        log.info("keys loaded");
        keysListView.setItems(keyStorage.getKeys());
        digestChoiceBox.setItems(FXCollections.observableArrayList(Digest.values()));
        digestChoiceBox.setValue(Digest.SHA256);
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
            log.info(file.getName());
        }
        String data = filesManager.readFromFile(file);
        if (data != null) {
            log.info("the " + file.getName() + " contains: \n" + data);
            log.info("set text for licenseTextArea:");
            licenseTextArea.textProperty().set(data);
        } else {
            log.warn("The file: " + file.getName() + " is empty");
        }
    }

    @FXML
    void handleSaveButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter txtExtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", ".txt");
        FileChooser.ExtensionFilter binExtFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", ".bin");
        FileChooser.ExtensionFilter base64ExtFilter = new FileChooser.ExtensionFilter("B64 files (*.base64)", ".base64");
        fileChooser.getExtensionFilters().addAll(base64ExtFilter, binExtFilter, txtExtFilter);
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
            File fileWithExtension = new File(file.getPath() + extension);
            log.info("save licence to file: " + fileWithExtension.getPath());
            licenseService.writeLicenceToFile(license, fileWithExtension, filesManager.getExtension(fileWithExtension));
        }
    }

    @FXML
    void handleSignButton(ActionEvent event) {
        PrivateKey privateKey = keysListView.getSelectionModel().getSelectedItem().getKeyPair().getPair().getPrivate();
        String digest = digestChoiceBox.getSelectionModel().getSelectedItem().value();
        license = License.Create.from(licenseTextArea.getText());
        licenseService.signLicence(license, privateKey, digest);
        licenseEncTextArea.textProperty().setValue(Base64.getEncoder().encodeToString(license.serialized()));
        log.info("sign happens");
    }

    @FXML
    void handleVerifyButton(ActionEvent event) {
        PublicKey publicKey = keysListView.getSelectionModel().getSelectedItem().getKeyPair().getPair().getPublic();
        InputStream inputStream = new ByteArrayInputStream(licenseEncTextArea.getText().getBytes(StandardCharsets.UTF_8));

        License myLicense = licenseService.readLicenceFromStream(inputStream);
        if (myLicense.isOK(publicKey)) {
            blinkTrue();
        } else {
            blinkFalse();
        }
    }

    @FXML
    void handleCloseMenuButton(ActionEvent event) {
        stage.close();
    }

    @FXML
    void handleDeleteKeyMenuButton(ActionEvent event) {
        Key selectedKey = keysListView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete key");
        alert.setHeaderText(selectedKey.getName());
        alert.setResizable(false);
        alert.setContentText("By deleting this key you won't be able to restore it any more");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) {
            log.info("delete alert was closed");
        } else if (result.get() == ButtonType.OK) {
            keyManager.deleteKeyFromRootFolder(selectedKey);
            keyStorage.deleteKey(selectedKey);
            log.info("delete key was approved");
        } else if (result.get() == ButtonType.CANCEL) {
            log.info("delete key was canceled");
        }
    }

    @FXML
    void handleAboutMenuButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About LicenseGen");
        alert.setHeaderText("Version: 0.1");
        alert.setContentText("This app is used to generate licence for applications");
        alert.showAndWait();
    }

    @FXML
    void handleUploadLicenseButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            log.info(file.getName());
        }
        License license = licenseService.readLicenceFromFile(file);
        if (license == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Licence Reader");
            alert.setContentText("Can't create license from file " + file.getName());
            alert.showAndWait();
        } else {
            licenseEncTextArea.textProperty().setValue(Base64.getEncoder().encodeToString(license.serialized()));
        }
    }

    @FXML
    void handleShowDumpPublicKey(ActionEvent event) {
        TextArea textArea = new TextArea("YOUR_MESSAGE_HERE");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);

        Key key = keysListView.getSelectionModel().getSelectedItem();
        textArea.textProperty().setValue(keyManager.dump(key.getKeyPair().getPublic()));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dump Key");
        alert.setHeaderText(key.getName());
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
    }

    @FXML
    void handleSaveDumpPublicKey(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter txtExtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", ".txt");
        fileChooser.getExtensionFilters().addAll(txtExtFilter);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
            File fileWithExtension = new File(file.getPath() + extension);
            log.info("save dump key to file: " + fileWithExtension.getPath());
            filesManager.writeToFile(
                    keyManager.dump(keysListView.getSelectionModel().getSelectedItem().getKeyPair().getPublic()),
                    fileWithExtension);
        }
    }

    private void blinkTrue() {
        log.info("blinking");
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(.5),
                        evt -> licenseEncTextArea.setStyle("text-area-background: " + format(Anim.trueColor) + ";"))
        );
        timeline.setCycleCount(2);
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            licenseEncTextArea.setStyle("text-area-background: " + format(Anim.defaultColor) + ";");
        });
    }

    void blinkFalse() {
        log.info("blinking");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.5), evt -> licenseEncTextArea.setStyle("text-area-background: " + format(Anim.falseColor) + ";")));
        timeline.setCycleCount(2);
        timeline.play();
        timeline.setOnFinished(actionEvent -> {
            licenseEncTextArea.setStyle("text-area-background: " + format(Anim.defaultColor) + ";");
        });
    }

    private String format(Color c) {
        int r = (int) (255 * c.getRed());
        int g = (int) (255 * c.getGreen());
        int b = (int) (255 * c.getBlue());
        return String.format("#%02x%02x%02x", r, g, b);
    }


    private void binding() {
        signButton.disableProperty().bind(
                keysListView.getSelectionModel().selectedItemProperty().isNull()
                        .or(
                                licenseTextArea.textProperty().isEmpty())
                        .or(
                                digestChoiceBox.getSelectionModel().selectedItemProperty().isNull())
                        .or(
                                Bindings.createBooleanBinding(() -> !licenseService.isLicense(licenseTextArea.textProperty()), licenseTextArea.textProperty())
                        ));

        saveButton.disableProperty().bind(
                licenseEncTextArea.textProperty().isEmpty()
        );

        verifyButton.disableProperty().bind(
                licenseEncTextArea.textProperty().isEmpty()
                        .or(
                                keysListView.getSelectionModel().selectedItemProperty().isNull())
        );

        deleteKeyMenuItem.disableProperty().bind(
                keysListView.getSelectionModel().selectedItemProperty().isNull()
        );

        showDumpPublicKeyMItem.disableProperty().bind(
                keysListView.getSelectionModel().selectedItemProperty().isNull()
        );
        saveDumpPublicKeyMItem.disableProperty().bind(
                keysListView.getSelectionModel().selectedItemProperty().isNull()
        );
    }
}
