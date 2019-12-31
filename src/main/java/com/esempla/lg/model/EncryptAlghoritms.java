package com.esempla.lg.model;

public enum EncryptAlghoritms {
    RSA("RSA");

    private String label;

    EncryptAlghoritms(String label){
        this.label = label;
    }


    public String toString() {
        return label;
    }
}
