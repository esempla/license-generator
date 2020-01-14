package com.esempla.lg.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax0.license3j.crypto.LicenseKeyPair;

public class Key {
    private final StringProperty name;
    private final ObjectProperty<LicenseKeyPair> keyPair;

    public Key() {
        this(null, null);
    }

    public Key(String name, LicenseKeyPair keyPair) {
        this.name = new SimpleStringProperty(name);
        this.keyPair = new SimpleObjectProperty<LicenseKeyPair>(keyPair);
    }

    public LicenseKeyPair getKeyPair() {
        return keyPair.get();
    }

    public void setKeyPair(LicenseKeyPair keyPair) {
        this.keyPair.set(keyPair);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<LicenseKeyPair> keyPairProperty() {
        return keyPair;
    }

    @Override
    public String toString() {
        return this.getName().toString();
    }

}
