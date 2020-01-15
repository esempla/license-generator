package com.esempla.lg.model;

public enum EncryptAlghoritms {
    RSA("RSA");
//    DSA("DSA"); //doesn't work well with verification


    private String label;

    EncryptAlghoritms(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }

    public String value() {
        return label;
    }
}
