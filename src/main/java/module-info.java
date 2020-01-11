module license.generator {
    requires license3j;
    requires org.slf4j;
    requires javafx.controls;
    requires javafx.fxml;
    opens com.esempla.lg.controller to javafx.fxml;
    exports com.esempla.lg to javafx.graphics;
}