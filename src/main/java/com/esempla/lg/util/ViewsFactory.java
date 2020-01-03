package com.esempla.lg.util;

import javafx.scene.Node;

import java.io.IOException;

public enum ViewsFactory {

    MAIN("/fxmls/main.fxml"),
    GEN_KEY_DIALOG("/fxmls/gen_key_dialog.fxml");

    public String path;

    ViewsFactory(String path) {
        this.path = path;
    }

    public Node getNode(FXMLLoaderProvider loader) throws IOException {
        return loader.get(path).load();
    }
}
