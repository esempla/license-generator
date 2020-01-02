package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import com.esempla.lg.model.Key;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrontPanelController {

    @FXML
    private ListView<Key> keysListView;


    private Launcher launcher;


    @FXML
    private void initialize(){
        log.info("initialize the front Panel");
    }

    public void setMainApp(Launcher launcher) {
        log.info("set main app in the front panel controller");
        this.launcher = launcher;
        keysListView.setItems(launcher.getKeys());

    }
}
