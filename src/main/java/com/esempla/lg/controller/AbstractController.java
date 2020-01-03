package com.esempla.lg.controller;

import com.esempla.lg.util.FXMLLoaderProvider;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;


@Getter
public abstract class AbstractController {

    protected final FXMLLoaderProvider provider;

    @Setter
    public Stage stage;

    public AbstractController(FXMLLoaderProvider provider) {
        this.provider = provider;
    }
}
