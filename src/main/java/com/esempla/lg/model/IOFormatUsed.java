package com.esempla.lg.model;

import javax0.license3j.io.IOFormat;

import java.util.ArrayList;
import java.util.List;

public enum IOFormatUsed {
    BINARY,
    BASE64;

    public static List<IOFormat> getMatch() {
        List<IOFormat> list = new ArrayList<>();
        for (IOFormatUsed format : IOFormatUsed.values()) {
            if (IOFormat.valueOf(format.toString()) != null) {
                list.add(IOFormat.valueOf(format.toString()));
            }
        }
        return list;
    }
}
