module com.example.demo29 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;

    opens com.example.demo29 to javafx.fxml;
    exports com.example.demo29;
}