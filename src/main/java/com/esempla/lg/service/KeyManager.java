package com.esempla.lg.service;

import com.esempla.lg.model.IOFormatUsed;
import com.esempla.lg.model.Key;
import com.esempla.lg.util.FileSystemUtil;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.KeyPairReader;
import javax0.license3j.io.KeyPairWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;


public class KeyManager {

    final static Logger log = LoggerFactory.getLogger(KeyManager.class);

    private FilesManager filesManager = new FilesManager();

    public List<Key> getKeysFromRootAppFolder(String path) {
        List<Key> keys = new ArrayList<>();
        for (File file : filesManager.listDirectories(path)) {
            Key key = loadKeyFromFolder(file.getName(), path);
            if (key != null)
                keys.add(key);
        }
        return keys;
    }

    public Key loadKeyFromFolder(String directoryName, String path) {
        Key key = new Key();
        key.setName(directoryName);
        File privateKeyFile =
                new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPrivateKeyName);
        File publicKeyFile =
                new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPublicKeyName);
        LicenseKeyPair privateLicenseKeyPair = loadPrivateKey(privateKeyFile);
        LicenseKeyPair publicLicenseKeyPair = loadPublicKey(publicKeyFile);
        if (privateLicenseKeyPair == null && publicLicenseKeyPair == null){
            return null;
        }
        key.setKeyPair(LicenseKeyPair.Create.from(
                publicLicenseKeyPair.getPair().getPublic(),
                privateLicenseKeyPair.getPair().getPrivate(), privateLicenseKeyPair.cipher())
        );
        return key;
    }

    public void deleteKeyFromRootFolder(Key key){
        File keyFolder =
                new File(FileSystemUtil.keysDirectoryPath + File.separator + key.getName());
        filesManager.deleteFolderAndContent(keyFolder);
    }

    public void writeKeyToFile(Key key, String path, IOFormat format) {
        filesManager.createDirectory(key.getName(), path);
        filesManager.createFile(FileSystemUtil.defaultPrivateKeyName, path + File.separator + key.getName());
        filesManager.createFile(FileSystemUtil.defaultPublicKeyName, path + File.separator + key.getName());

        File privateKeyFile = new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPrivateKeyName);
        File publicKeyFile = new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPublicKeyName);

        try {
            KeyPairWriter keyPairWriter = new KeyPairWriter(privateKeyFile, publicKeyFile);
            keyPairWriter.write(key.getKeyPair(), format);
        } catch (IOException e) {
           log.error("Error on write key to file {}.", e.getMessage());
        }
    }

    private LicenseKeyPair loadPrivateKey(File file) {
        KeyPairReader keyPairReader = null;
        try {
            keyPairReader = new KeyPairReader(file);
        } catch (FileNotFoundException e) {
            log.error("Can't create the keyPairReader {} ", e.getMessage());
        }
        for (IOFormat format : IOFormatUsed.getMatch()) {
            try {
                LicenseKeyPair licenseKeyPair = null;
                if (keyPairReader != null) {
                    (new KeyPairReader(file)).readPrivate(format);
                    licenseKeyPair = keyPairReader.readPrivate(format);
                }
                if (licenseKeyPair != null) {

                    return licenseKeyPair;
                }
            } catch (Exception e) {
                log.error("Error on load private key {} ", e.getMessage());
            }
        }
        return null;
    }

    private LicenseKeyPair loadPublicKey(File file) {
        KeyPairReader keyPairReader = null;
        try {
            keyPairReader = new KeyPairReader(file);
        } catch (FileNotFoundException e) {
            log.error("Error on load public key {} ", e.getMessage());
        }
        for (IOFormat format : IOFormatUsed.getMatch()) {
            try {
                LicenseKeyPair licenseKeyPair = null;
                if (keyPairReader != null) {
                    (new KeyPairReader(file)).readPublic(format);
                    licenseKeyPair = keyPairReader.readPublic(format);
                }
                if (licenseKeyPair != null) {
                    return licenseKeyPair;
                }
            } catch (Exception e) {
                log.error("Error on load private key {} ", e.getMessage());
            }
        }
        return null;
    }
    public String dump(byte[] key){

        Formatter formatter = new Formatter();
        for (byte b : key) {
            formatter.format( "(byte) 0x%02X, ", b);
        }
       return formatter.toString();
    }
}
