package com.esempla.lg.util;

import javafx.fxml.FXMLLoader;
import lombok.Getter;


public class FXMLLoaderProvider {
    @Getter
    private FXMLLoader loader;

    public FXMLLoader get(String path) {
        loader = new FXMLLoader(getClass().getResource(path));
        return loader;
    }
}
