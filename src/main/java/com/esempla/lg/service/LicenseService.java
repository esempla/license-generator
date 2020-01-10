package com.esempla.lg.service;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javax0.license3j.License;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

@Slf4j
public class LicenseService {
    public boolean isLicense(String string){
        log.info("checking if the string: "+string+" can be considered as a license");
        try{
            License.Create.from(string);
            log.info("can create the license");
        } catch (Exception e) {
            log.info("can not create the license");
            return false;
        }
        return true;
    }
    public boolean isLicense(StringProperty simpleStringProperty){
        log.info("checking if the string: "+simpleStringProperty.getValue()+" can be considered as a license");
        try{
            License.Create.from(simpleStringProperty.getValue());
            log.info("can create the license");
        } catch (Exception e) {
            log.info("can not create the license");
            return false;
        }
        return true;
    }
    public boolean signLicence (License license, PrivateKey key, String digest){
        try {
            license.sign(key,digest);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
