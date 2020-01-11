package com.esempla.lg.service;

import com.esempla.lg.model.Key;
import javax0.license3j.crypto.LicenseKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


public class KeyManager {

    final static Logger log = LoggerFactory.getLogger(KeyManager.class);

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

    public Key loadKeyFromFolder(String directoryName, String path) {
        Key key = new Key();
        byte[] privateBytes = new byte[0];
        byte[] publicBytes = new byte[0];
        key.setName(directoryName);

        for (File file : filesManager.listFiles(path + File.separator + directoryName)) {
            if (file.getName().contains("pub")) {
                publicBytes = filesManager.readContentIntoByteArray(file);
            } else {
                privateBytes = filesManager.readContentIntoByteArray(file);
            }
        }
        try {
            key.setKeyPair(LicenseKeyPair.Create.from(privateBytes, publicBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    public boolean writeKeyToFile(Key key, String path) {
        filesManager.createDirectory(key.getName(), path);
        filesManager.createFile("key", path + File.separator + key.getName());
        filesManager.createFile("key.pub", path + File.separator + key.getName());
        File file = new File(path + File.separator + key.getName() + File.separator + "key");
        filesManager.writeByteArrayToFile(key.getKeyPair().getPrivate(), file);
        File file2 = new File(path + File.separator + key.getName() + File.separator + "key.pub");
        filesManager.writeByteArrayToFile(key.getKeyPair().getPublic(), file2);
        return true;
    }

}
