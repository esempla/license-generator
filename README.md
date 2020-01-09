# license-generator

License generator desktop application

## Package (Linux)

export JDK_14=/opt/jdk-14/
export PATH_TO_FX=/opt/javafx-sdk-13/lib/
export PATH_TO_FX_MODS=/opt/javafx-jmods-13/

javac --module-path $PATH_TO_FX \
    --add-modules javafx.controls,javafx.fxml \
    --class-path libs/lombok-1.18.8.jar:libs/license3j-3.1.0.jar:libs/slf4j-api-1.7.25.jar \
    -d out $(find src -name "*.java")

java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml \
    --class-path out:libs/lombok-1.18.8.jar:libs/license3j-3.1.0.jar:libs/slf4j-api-1.7.25.jar \
    com.esempla.lg.Launcher

jar --create --file=libs/licensegen.jar --main-class=com.esempla.lg.Launcher -C out .

$JDK_14/bin/jpackage --type deb --dest installer -i libs \
    --main-jar licensegen.jar -n licensegen \
    --module-path $PATH_TO_FX_MODS \
    --linux-menu-group Office \
    --linux-shortcut \
    --add-modules javafx.controls,javafx.fxml --main-class com.esempla.lg.Launcher