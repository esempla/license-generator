package com.esempla.lg;

import com.esempla.lg.controller.FrontPanelController;
import com.esempla.lg.model.Key;
import com.esempla.lg.service.FilesManager;
import com.esempla.lg.service.KeyManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax0.license3j.crypto.LicenseKeyPair;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


@Slf4j
public class Launcher extends Application {


    private String appHomeDir = ".licenseGenerator";
    private String homeURL =  System.getProperty("user.home").toString()+ File.separator+appHomeDir;
    private String keysHomeDirectory = ".keys";
    private String logsHomeDirectory = ".logs";
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Key> keys = FXCollections.observableArrayList();


    public Launcher(){
        if (homeExists()) {
            log.info("check if the keys home folder exists. If not, it is created");
            createKeysHomeFolder();
            log.info("Load keys from "+homeURL);

            loadKeys();
        }else{
            log.info("Created the home directory: Path: "+homeURL);
            createHomeDirectory();
        }
    }

    private void createKeysHomeFolder() {
        if (!FilesManager.isInDirectory(keysHomeDirectory,homeURL)){
            FilesManager.createDirectory(keysHomeDirectory,homeURL);
        }
    }

    private void createHomeDirectory() {
        FilesManager.createDirectory(appHomeDir,System.getProperty("user.home").toString());
        FilesManager.createDirectory(keysHomeDirectory,homeURL);
        FilesManager.createDirectory(logsHomeDirectory,homeURL);
    }

    private boolean homeExists() {
        return FilesManager.isInDirectory(appHomeDir,System.getProperty("user.home").toString());

    }

    private void loadKeys() {
//        LicenseKeyPair myKey = KeyManager.getLicenseKeyPair("RSA",1024);
//        Key mkey = new Key("prima",myKey);
//        keys.add(mkey);
//        KeyManager.writeKeyToFile(mkey,homeURL+File.separator+keysHomeDirectory);
//        log.info("AddedKeysToFile");
//       keys.addAll(Objects.requireNonNull(KeyManager.getKeysFromRootAppFolder(homeURL + File.separator + keysHomeDirectory)));
        log.info("hhah");
    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        log.info("Create the primarty stage");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("License generator");
        log.info("init the root layout");
        initRootLayout();
        log.info("init the front Panel");
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


    public ObservableList<Key> getKeys() {
        return keys;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
