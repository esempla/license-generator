package com.esempla.lg.service;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javax0.license3j.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.LicenseReader;
import javax0.license3j.io.LicenseWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class LicenseService {

    final static Logger log = LoggerFactory.getLogger(LicenseService.class);

    public SimpleBooleanProperty isLicense(StringProperty simpleStringProperty) {
        log.info("checking if the string: \" " + simpleStringProperty.getValue() + "\" can be considered as a license");
        try {
            License.Create.from(simpleStringProperty.getValue());
    private FilesManager filesManager = new FilesManager();
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

    public boolean signLicence(License license, PrivateKey key, String digest) {
        try {
            license.sign(key, digest);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean writeLicenceToFile(License license, File file, IOFormat format){
        LicenseWriter licenseWriter = null;
        try {
            licenseWriter = new LicenseWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        try {
            licenseWriter.write(license,format);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }
    public License readLicenceFromFile(File file){
        LicenseReader licenseReader = null;
        try {
            licenseReader = new LicenseReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return  licenseReader.read(filesManager.getExtension(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public License readLicenceFromStream(InputStream inputStream){
        LicenseReader licenseReader = new LicenseReader(inputStream);

        try {
            return  licenseReader.read(IOFormat.BASE64);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
