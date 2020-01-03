package com.esempla.lg;

import com.esempla.lg.controller.RootLayoutController;
import com.esempla.lg.util.FileSystemUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Launcher extends Application {


    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private static Stage primaryStage;
    private FileSystemUtil fileSystemUtil = new FileSystemUtil();
    private RootLayoutController rootLayoutController = new RootLayoutController();


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

        rootLayoutController.setPrimaryStage(this.primaryStage);
        rootLayoutController.initRootLayout();
    }

}
