module de.herrvoennchen.fxsmtp {
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
    requires org.slf4j.simple;
    requires org.yaml.snakeyaml;
    requires org.slf4j;

    opens de.herrvoennchen.fxsmtp to javafx.fxml;
    exports de.herrvoennchen.fxsmtp;
    exports de.herrvoennchen.fxsmtp.core;
    exports de.herrvoennchen.fxsmtp.model;
    exports de.herrvoennchen.fxsmtp.server;
}