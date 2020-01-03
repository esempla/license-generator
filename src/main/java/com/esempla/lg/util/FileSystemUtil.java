package com.esempla.lg.util;

import com.esempla.lg.service.FilesManager;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileSystemUtil {

    public static String appHomeDir = ".licenseGenerator";
    public static String homeURL = System.getProperty("user.home") + File.separator + appHomeDir;
    public static String keysHomeDirectory = ".keys";
    public static String logsHomeDirectory = ".logs";
    private FilesManager filesManager = new FilesManager();


    public void init() {
        if (homeExists()) {
            log.info("check if the keys home folder exists. If not, it is created");
            createKeysHomeFolder();
            log.info("Load keys from " + homeURL);

            loadKeys();
        } else {
            log.info("Created the home directory: Path: " + homeURL);
            createHomeDirectory();
        }
    }

    private  void createKeysHomeFolder() {
        if (!filesManager.isInDirectory(keysHomeDirectory, homeURL)) {
            filesManager.createDirectory(keysHomeDirectory, homeURL);
        }
    }

    private  void createHomeDirectory() {
        filesManager.createDirectory(appHomeDir, System.getProperty("user.home").toString());
        filesManager.createDirectory(keysHomeDirectory, homeURL);
        filesManager.createDirectory(logsHomeDirectory, homeURL);
    }

    private  boolean homeExists() {
        return filesManager.isInDirectory(appHomeDir, System.getProperty("user.home").toString());

    }

    private void loadKeys() {
//        LicenseKeyPair myKey = KeyManager.getLicenseKeyPair("RSA",1024);
//        Key mkey = new Key("prima",myKey);
//        keys.add(mkey);
//        KeyManager.writeKeyToFile(mkey,homeURL+File.separator+keysHomeDirectory);
//        log.info("AddedKeysToFile");
//       keys.addAll(Objects.requireNonNull(KeyManager.getKeysFromRootAppFolder(homeURL + File.separator + keysHomeDirectory)));
        log.info("hhah");
    }


}
