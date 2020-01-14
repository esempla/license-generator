package com.esempla.lg.util;


import com.esempla.lg.model.Key;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KeyStorage {

    private static KeyStorage instance;

    private KeyStorage() {
    }

    public static KeyStorage getInstance() {
        if (instance == null) {
            instance = new KeyStorage();
        }
        return instance;
    }

    public ObservableList<Key> getKeys() {
        return keys;
    }

    public void addKey(Key key) {
        this.keys.add(key);
    }

    private ObservableList<Key> keys = FXCollections.observableArrayList();
}