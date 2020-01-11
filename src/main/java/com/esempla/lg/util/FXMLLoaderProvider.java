package com.esempla.lg.util;

import javafx.fxml.FXMLLoader;

public class FXMLLoaderProvider {

    private FXMLLoader loader;

    public FXMLLoader get(String path) {
        loader = new FXMLLoader(getClass().getResource(path));
        return loader;
    }

    public FXMLLoader getLoader() {
        return loader;
    }
}
