package com.esempla.lg.service;

import javafx.beans.property.StringProperty;
import javax0.license3j.License;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.LicenseReader;
import javax0.license3j.io.LicenseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

public class LicenseService {

    final static Logger log = LoggerFactory.getLogger(LicenseService.class);

    private FilesManager filesManager;

    public LicenseService() {
        this.filesManager = new FilesManager();
    }

    public boolean isLicense(String string) {
        log.info("checking if the string: " + string + " can be considered as a license.");
        try {
            License.Create.from(string);
            log.info("can create the license");
        } catch (Exception e) {
            log.error("Error. Can not create the license");
            return false;
        }
        return true;
    }

    public boolean isLicense(StringProperty simpleStringProperty) {
        log.info("checking if the string: " + simpleStringProperty.getValue() + " can be considered as a license.");
        try {
            License.Create.from(simpleStringProperty.getValue());
            log.info("can create the license");
        } catch (Exception e) {
            log.error("Error. Can not create the license.");
            return false;
        }
        return true;
    }

    public boolean signLicence(License license, PrivateKey key, String digest) {
        try {
            license.sign(key, digest);
        } catch (Exception e) {
            log.error("Error");
            return false;
        }
        return true;
    }

    public boolean writeLicenceToFile(License license, File file, IOFormat format) {
        LicenseWriter licenseWriter = null;
        try {
            licenseWriter = new LicenseWriter(file);
        } catch (FileNotFoundException e) {
            log.error("Error on write licence to file : {}.", e.getMessage());
            return false;
        }
        try {
            licenseWriter.write(license, format);
        } catch (IOException e) {
            log.error("Error on write licence to file : {}.", e.getMessage());
            return false;
        }
        return true;
    }

    public License readLicenceFromFile(File file) {
        LicenseReader licenseReader = null;
        try {
            licenseReader = new LicenseReader(file);
        } catch (FileNotFoundException e) {
            log.error("Error on read licence from file : {}.", e.getMessage());
            return null;
        }
        try {
            License license = null;
            license = licenseReader.read(filesManager.getExtension(file));
            return license;
        } catch (IOException e) {
            log.error("Error on read licence from file : {}.", e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            log.error("Can't create licence from file : {}.", e.getMessage());
            return null;
        }
    }

    public License readLicenceFromStream(InputStream inputStream) {
        LicenseReader licenseReader = new LicenseReader(inputStream);
        try {
            return licenseReader.read(IOFormat.BASE64);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
