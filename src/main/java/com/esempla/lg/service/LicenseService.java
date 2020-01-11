package com.esempla.lg.service;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javax0.license3j.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class LicenseService {

    final static Logger log = LoggerFactory.getLogger(LicenseService.class);

    public SimpleBooleanProperty isLicense(StringProperty simpleStringProperty) {
        log.info("checking if the string: \" " + simpleStringProperty.getValue() + "\" can be considered as a license");
        try {
            License.Create.from(simpleStringProperty.getValue());
            log.info("can create the license");
        } catch (Exception e) {
            log.info("can not create the license");
            return new SimpleBooleanProperty(false);
        }
        return new SimpleBooleanProperty(true);
    }

    public boolean isLicense(String string) {
        log.info("checking if the string: " + string + " can be considered as a license");
        try {
            License.Create.from(string);
            log.info("can create the license");
        } catch (Exception e) {
            log.info("can not create the license");
            return false;
        }
        return true;
    }

    public boolean signLicence(License license, PrivateKey key, String digest) {
        try {
            license.sign(key, digest);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
