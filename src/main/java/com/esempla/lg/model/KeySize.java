package com.esempla.lg.model;

import javafx.beans.property.IntegerProperty;

public enum KeySize {
    S1024(1024),
    S2048(2048);

    private Integer size;

    KeySize(Integer label){
        this.size = label;
    }

    public String toString() {
        return size.toString();
    }
    public Integer getSize(){
        return size;
    }


}

