package com.esempla.lg.service;

import com.esempla.lg.model.IOFormatUsed;
import com.esempla.lg.model.Key;
import com.esempla.lg.util.FileSystemUtil;
import javax0.license3j.crypto.LicenseKeyPair;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.KeyPairReader;
import javax0.license3j.io.KeyPairWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KeyManager {

    private FilesManager filesManager = new FilesManager();

    public LicenseKeyPair getLicenseKeyPair(String alg, Integer size) {
        log.info("getHere");
        LicenseKeyPair mykeys = null;
        try {
            mykeys = LicenseKeyPair.Create.from(alg, size);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mykeys;
    }

    public List<Key> getKeysFromRootAppFolder(String path) {
        List<Key> keys = new ArrayList<>();
        for (File file : filesManager.listDirectories(path)) {
            keys.add(loadKeyFromFolder(file.getName(), path));
        }
        return keys;
    }

    public Key loadKeyFromFolder(String directoryName, String path){
        Key key = new Key();
        key.setName(directoryName);
        File privateKeyFile = new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPrivateKeyName);
        File publicKeyFile = new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPublicKeyName);
        LicenseKeyPair privateLicenseKeyPair = loadPrivateKey(privateKeyFile);
        LicenseKeyPair publicLicenseKeyPair = loadPublicKey(publicKeyFile);
        key.setKeyPair(LicenseKeyPair.Create.from(publicLicenseKeyPair.getPair().getPublic(),privateLicenseKeyPair.getPair().getPrivate(),privateLicenseKeyPair.cipher()));

        return key;

    }

    public boolean writeKeyToFile(Key key, String path, IOFormat format) {
        filesManager.createDirectory(key.getName(),path);
        filesManager.createFile(FileSystemUtil.defaultPrivateKeyName, path + File.separator + key.getName());
        filesManager.createFile(FileSystemUtil.defaultPublicKeyName, path + File.separator + key.getName());

        File privateKeyFile = new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPrivateKeyName);
        File publicKeyFile = new File(path + File.separator + key.getName() + File.separator + FileSystemUtil.defaultPublicKeyName);

        try {
            KeyPairWriter keyPairWriter = new KeyPairWriter(privateKeyFile,publicKeyFile);
            keyPairWriter.write(key.getKeyPair(),format);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return  true;
    }

    private LicenseKeyPair loadPrivateKey(File file){
        KeyPairReader keyPairReader = null;
        try {
            keyPairReader = new KeyPairReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (IOFormat format: IOFormatUsed.getMatch()){
           try {
               LicenseKeyPair licenseKeyPair = null;
               if (keyPairReader != null) {
                   (new KeyPairReader(file)).readPrivate(format);
                   licenseKeyPair = keyPairReader.readPrivate(format);
               }
               if (licenseKeyPair != null) {
                   return licenseKeyPair;
               }
           } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | IllegalArgumentException e) {
               e.printStackTrace();
           }
       }
        return null;
    }
    private LicenseKeyPair loadPublicKey(File file){
        KeyPairReader keyPairReader = null;
        try {
            keyPairReader = new KeyPairReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (IOFormat format: IOFormatUsed.getMatch()){
            try {
                LicenseKeyPair licenseKeyPair = null;
                if (keyPairReader != null) {
                    (new KeyPairReader(file)).readPublic(format);
                    licenseKeyPair = keyPairReader.readPublic(format);
                }
                if (licenseKeyPair != null) {
                    return licenseKeyPair;
                }
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException |  IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
