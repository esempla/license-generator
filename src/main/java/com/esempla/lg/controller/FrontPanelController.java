package com.esempla.lg.controller;

import com.esempla.lg.Launcher;
import javafx.fxml.FXML;

public class FrontPanelController {




    private Launcher launcher;


    @FXML
    private void initialize(){

    }

    public void setMainApp(Launcher launcher) {
        this.launcher = launcher;
    }
}
