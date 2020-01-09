package com.esempla.lg.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax0.license3j.License;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LicenseService {
    public SimpleBooleanProperty isLicense(StringProperty simpleStringProperty){
        log.info("checking if the string: \" "+simpleStringProperty.getValue()+"\" can be considered as a license");
        try{
            License.Create.from(simpleStringProperty.getValue());
            log.info("can create the license");
        } catch (Exception e) {
            log.info("can not create the license");
            return new SimpleBooleanProperty(false);
        }
        return new SimpleBooleanProperty(true);
    }
    public boolean isLicense(String string){
        log.info("checking if the string: "+string+" can be considered as a license");
        try{
            License.Create.from(string);
            log.info("can create the license");
        } catch (Exception e) {
            log.info("can not create the license");
            return false;
        }
        return true;
    }
}
