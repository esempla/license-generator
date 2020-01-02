package com.esempla.lg.service;

import lombok.extern.slf4j.Slf4j;

import javax.print.attribute.standard.PresentationDirection;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j


//the class manages files
public class FilesManager {

    //returns all the files from a directory
    public static List<File> listFiles(String directoryName) {
        log.info("gets all the files from "+directoryName);

        File directory = new File(directoryName);
        List<File> files = new ArrayList<>();
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        return files;
    }

    //returns all the directories from a directory
    public  static List<File> listDirectories(String directoryName) {
        log.info("get all directories from "+directoryName);

        File directory = new File(directoryName);
        List<File> directories = new ArrayList<>();

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isDirectory()) {
                    directories.add(file);
                }
            }
        return directories;
    }
    public static boolean isInDirectory(String fileName, String directoryName){
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.getName().equals(fileName)) {
                    return true;
                }
            }
        return false;
    }
    public static boolean createDirectory(String name, String path){
        File theDir = new File(path+File.separator+name);
        boolean result = false;
// if the directory does not exist, create it
        if (!theDir.exists()) {
            log.info("creating directory: " + theDir.getName());


            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
                log.info("creation of directory failed");
            }
            if(result) {
                log.info(theDir.getName()+" created");
            }
        }
        return result;
    }
    public static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++)
            {
                System.out.print((char) bFile[i]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }
    public static boolean writeByteArrayToFile(byte[] bytes, File file){
        FileOutputStream output = null;
        boolean success = false;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
            output.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }


    public  static boolean createFile(String name, String path){
        boolean success = false;
        try {
            File file = new File(path+File.separator+name);
            boolean fvar = file.createNewFile();
            if (fvar){
                log.info("File has been created successfully");

            }
            else{
                log.info("File already present at the specified location");
            }
            success = true;
        } catch (IOException e) {
            log.info("Exception Occurred in creation the file:");
            e.printStackTrace();
        }
        return success;
    }
}
