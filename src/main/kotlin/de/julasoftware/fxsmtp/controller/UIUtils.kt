package de.julasoftware.fxsmtp.controller

import de.julasoftware.fxsmtp.FxSmtpServerApplication
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.TextFormatter
import javafx.stage.Stage
import java.util.*
import java.util.function.UnaryOperator

val integerFilter: UnaryOperator<TextFormatter.Change> = UnaryOperator<TextFormatter.Change> { change: TextFormatter.Change ->
    val demoText: String = change.controlNewText
    if (demoText.matches("-?([1-9][0-9]*)?".toRegex())) {
        return@UnaryOperator change
    } else if ("-" == change.text) {
        if (change.controlText.startsWith("-")) {
            change.setText("")
            change.setRange(0, 1)
            change.setCaretPosition(change.caretPosition - 2)
            change.setAnchor(change.anchor - 2)
            return@UnaryOperator change
        } else {
            change.setRange(0, 0)
            return@UnaryOperator change
        }
    }
    null
}

fun createWindowStage(
    xmlViewFileName: String,
    title: String = "FxSMTP Server",
    width: Double = 400.0,
    height: Double = 300.0,
    preCreatedStage: Stage? = null,
    alwaysOnTop: Boolean = false
): Stage {
    val stage = preCreatedStage ?: Stage()
    val fxmlLoader = FXMLLoader(FxSmtpServerApplication::class.java.getResource(xmlViewFileName))
    fxmlLoader.resources = ResourceBundle.getBundle("bundles/Messages", Locale.getDefault())
    val scene = Scene(fxmlLoader.load(), width, height)
    stage.userData = fxmlLoader.getController()
    stage.title = title
    stage.scene = scene
    stage.isAlwaysOnTop = alwaysOnTop

    return stage
}