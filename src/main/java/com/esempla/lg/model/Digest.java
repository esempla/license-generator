package com.esempla.lg.model;

public enum Digest {
    SHA512("SHA-512"),
    SHA256("SHA-256");

    private String label;

    Digest(String label) {
        this.label = label;
    }


    public String toString() {
        return label;
    }

    public String value() {
        return label;
    }
}
