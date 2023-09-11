package de.julasoftware.fxsmtp

import de.julasoftware.fxsmtp.controller.MainViewController
import de.julasoftware.fxsmtp.controller.createWindowStage
import javafx.application.Application
import javafx.stage.Stage

class FxSmtpServerApplication : Application() {

    override fun start(stage: Stage) {
        createWindowStage("main-view.fxml", width = 800.0, height = 600.0, preCreatedStage = stage)
        stage.setOnCloseRequest { (stage.userData as MainViewController).shutdown() }
        stage.show()
    }
}

fun main(args: Array<String>) {
    System.setProperty("mail.mime.decodetext.strict", "false");
    Application.launch(FxSmtpServerApplication::class.java)
}