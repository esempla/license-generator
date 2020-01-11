# license-generator
License generator desktop application

## Package (Linux)

### Requirements
[Insatall JDK 14](https://jdk.java.net/14/)<br>
[Insatall JavaFX Linux SDK 13.0.1](http://gluonhq.com/download/javafx-13.0.1-sdk-linux/)<br>
[Insatall JavaFX Linux jmods 13.0.1](https://gluonhq.com/download/javafx-13.0.1-jmods-linux/)<br>

#### Export these environment variables:
    export JAVA_HOME=/opt/jdk-14/
    export PATH_TO_FX=/opt/javafx-sdk-13/lib/
    export PATH_TO_FX_MODS=/opt/javafx-jmods-13/

#### Compile your application:
    javac --module-path $PATH_TO_FX \
        --add-modules javafx.controls,javafx.fxml \
        --class-path libs/lombok-1.18.8.jar:libs/license3j-3.1.0.jar:libs/slf4j-api-1.7.25.jar \
        -d out $(find src -name "*.java")        

#### Copy fxml files:
    cp -r src/main/resources/fxmls out

#### Run and test:
    java --module-path $PATH_TO_FX \
        --add-modules javafx.controls,javafx.fxml \
        --class-path out:libs/lombok-1.18.8.jar:libs/license3j-3.1.0.jar:libs/slf4j-api-1.7.25.jar \
        com.esempla.lg.Launcher

#### Create a jar:
    jar --create --file=libs/licensegen.jar --main-class=com.esempla.lg.Launcher -C out .

#### Create the installer:
    $JAVA_HOME/bin/jpackage \
        --type deb --dest installer -i libs \
        --main-jar licensegen.jar -n LicenseGen \
        --module-path $PATH_TO_FX_MODS \
        --linux-menu-group Utility \
        --linux-deb-maintainer admin@esempla.com \
        --linux-shortcut \
        --icon src/main/resources/icon.png \
        --add-modules javafx.controls,javafx.fxml \
        --main-class com.esempla.lg.Launcher
        
### Modular (command line)

#### Compile your application:
    javac --module-path $PATH_TO_FX:mods \
        --add-modules license3j  \
        -d mods/license.generator $(find src -name "*.java")

#### Run and test:    
    java --module-path $PATH_TO_FX:mods -m license.generator/com.esempla.lg.Launcher

#### Create custom image:        
    $JAVA_HOME/bin/jlink --module-path $PATH_TO_FX_MODS:mods \
        --add-modules license.generator \
        --compress 2 \
        --strip-debug \
        --no-header-files \
        --no-man-pages \
        --output image
        
#### Create the installer:        
     $JAVA_HOME/bin/jpackage \
            --type deb --dest installer \
            --name LicenseGen \
            --linux-menu-group Utility \
            --linux-deb-maintainer admin@esempla.com \
            --linux-shortcut \
            --icon src/main/resources/icon.png \
            --module license.generator/com.esempla.lg.Launcher \
            --runtime-image image
            
### Modular (maven)
        