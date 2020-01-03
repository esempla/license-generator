package com.esempla.lg;

import com.esempla.lg.controller.RootLayoutController;
import com.esempla.lg.model.Key;
import com.esempla.lg.service.FilesManager;
import com.esempla.lg.util.FileSystemUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Launcher extends Application {


    private Stage primaryStage;
    private FileSystemUtil fileSystemUtil = new FileSystemUtil();



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        fileSystemUtil.init();

    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Create the primarty stage");
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("License generator");
        log.info("init the root layout");

        new RootLayoutController().initRootLayout(primaryStage);
    }


}
