package com.esempla.lg.service;

import com.esempla.lg.model.Key;
import javax0.license3j.crypto.LicenseKeyPair;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
public class KeyGenerator {

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
}
