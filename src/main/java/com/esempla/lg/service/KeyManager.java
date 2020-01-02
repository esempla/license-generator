package com.esempla.lg.service;

import com.esempla.lg.model.Key;
import javax0.license3j.crypto.LicenseKeyPair;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Slf4j
public class KeyManager {

    public static LicenseKeyPair getLicenseKeyPair(String alg, Integer size){
      log.info("getHere");
        LicenseKeyPair mykeys = null;
        try {
             mykeys = LicenseKeyPair.Create.from(alg,size);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mykeys;
    }
    public static List<Key> getKeysFromRootAppFolder(){

        return null;
    }
    public static Key loadKeyFromFolder(String directoryName, String path) {
        Key key = new Key();
        byte[] privateBytes = new byte[0];
        byte[] publicBytes = new byte[0];
        key.setName(directoryName);

        for (File file : FilesManager.listFiles(path + File.separator + directoryName)) {
            if (file.getName().contains("pub")){
                publicBytes = FilesManager.readContentIntoByteArray(file);
            }else{
                privateBytes = FilesManager.readContentIntoByteArray(file);
            }
        }
        try {
            key.setKeyPair(LicenseKeyPair.Create.from(privateBytes,publicBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return  key;
    }
    public static boolean writeKeyToFile(Key key, String path){
        FilesManager.createDirectory(key.getName(),path);
        FilesManager.createFile("key",path+File.separator+key.getName());
        FilesManager.createFile("key.pub",path+File.separator+key.getName());
        File file = new File(path+File.separator+key.getName()+File.separator+"key");
        FilesManager.writeByteArrayToFile(key.getKeyPair().getPrivate(),file);
        File file2 = new File(path+File.separator+key.getName()+File.separator+"key.pub");
        FilesManager.writeByteArrayToFile(key.getKeyPair().getPublic(),file2);
        return true;
    }

}
