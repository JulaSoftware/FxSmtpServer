package de.herrvoennchen.fxsmtp

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class FxSmtpServerApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(FxSmtpServerApplication::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 800.0, 600.0)
        stage.title = "FxSMTP Server"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(FxSmtpServerApplication::class.java)
}