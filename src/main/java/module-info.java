module com.example.fxsmtpserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires subethasmtp;
    requires slf4j.api;

    opens de.herrvoennchen.fxsmtp to javafx.fxml;
    exports de.herrvoennchen.fxsmtp;
}