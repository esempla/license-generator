package com.esempla.lg.service;

import javax0.license3j.crypto.LicenseKeyPair;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;

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
}
