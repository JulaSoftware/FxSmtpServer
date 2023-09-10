module de.julasoftware.fxsmtp {
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

    opens de.julasoftware.fxsmtp to javafx.fxml;
    opens de.julasoftware.fxsmtp.controller to javafx.fxml;
    exports de.julasoftware.fxsmtp;
    exports de.julasoftware.fxsmtp.core;
    exports de.julasoftware.fxsmtp.model;
    exports de.julasoftware.fxsmtp.server;
    exports de.julasoftware.fxsmtp.controller;
}