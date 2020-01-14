package com.esempla.lg;

import com.esempla.lg.controller.MainController;
import com.esempla.lg.util.FXMLLoaderProvider;
import com.esempla.lg.util.FileSystemUtil;
import com.esempla.lg.util.ViewsFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Launcher extends Application {

    final static Logger log = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        new FileSystemUtil().init();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        log.info("Create the primarty stage");
        primaryStage.setTitle("License generator");

        FXMLLoaderProvider provider = new FXMLLoaderProvider();
        BorderPane borderPane = (BorderPane) ViewsFactory.MAIN.getNode(provider);

        MainController controller = provider.getLoader().getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").
                toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
