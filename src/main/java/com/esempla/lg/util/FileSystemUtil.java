package com.esempla.lg.util;

import com.esempla.lg.model.Key;
import com.esempla.lg.service.FilesManager;
import com.esempla.lg.service.KeyManager;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileSystemUtil {

    public static String appHomeDir = ".licenseGenerator";
    public static String homeURL = System.getProperty("user.home") + File.separator + appHomeDir;
    public static String keysDirectory = "keys";
    public static String logsHomeDirectory = "logs";
    public static String keysDirectoryPath = homeURL + File.separator + keysDirectory;
    public static String defaultPublicKeyName = "public.key";
    public static String defaultPrivateKeyName = "private.key";
    private FilesManager filesManager = new FilesManager();
    private KeyManager keyManager = new KeyManager();


    public void init() {
        if (homeExists()) {
            log.info("check if the keys home folder exists. If not, it is created");
            createKeysHomeFolder();
            log.info("Load keys from " + homeURL);

//            loadKeys();
        } else {
            log.info("Created the home directory: Path: " + homeURL);
            createHomeDirectory();
        }
    }

    private void createKeysHomeFolder() {
        if (!filesManager.isInDirectory(keysDirectory, homeURL)) {
            filesManager.createDirectory(keysDirectory, homeURL);
        }
    }

    private void createHomeDirectory() {
        filesManager.createDirectory(appHomeDir, System.getProperty("user.home").toString());
        filesManager.createDirectory(keysDirectory, homeURL);
        filesManager.createDirectory(logsHomeDirectory, homeURL);
    }

    private boolean homeExists() {
        return filesManager.isInDirectory(appHomeDir, System.getProperty("user.home").toString());
    }

    public List<Key> loadKeys() {
        List<Key> keys = new ArrayList<>();
        keys = Objects.requireNonNull(keyManager.getKeysFromRootAppFolder(keysDirectoryPath));
        return keys;
    }
}
