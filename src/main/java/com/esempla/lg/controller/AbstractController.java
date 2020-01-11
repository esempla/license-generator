package com.esempla.lg.controller;

import com.esempla.lg.util.FXMLLoaderProvider;
import javafx.stage.Stage;

public abstract class AbstractController {

    protected final FXMLLoaderProvider provider;

    public Stage stage;

    public AbstractController(FXMLLoaderProvider provider) {
        this.provider = provider;
    }

    public FXMLLoaderProvider getProvider() {
        return provider;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
