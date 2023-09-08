package de.herrvoennchen.fxsmtp

import de.herrvoennchen.fxsmtp.core.Configuration
import de.herrvoennchen.fxsmtp.server.SmtpServer
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter
import java.util.function.UnaryOperator


class MainViewController {
    @FXML
    private lateinit var startButton: Button

    @FXML
    private lateinit var settingsButton: Button

    @FXML
    private lateinit var portField: TextField

    @FXML
    private lateinit var statusLabel: Label


    private var integerFilter: UnaryOperator<TextFormatter.Change> = UnaryOperator<TextFormatter.Change> { change: TextFormatter.Change ->
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

    private var stringConverter: StringConverter<Int> = object : IntegerStringConverter() {
        override fun fromString(s: String): Int {
            return if (s.isEmpty()) 0 else super.fromString(s)
        }
    }

    @FXML
    fun initialize() {
        portField.textFormatter = TextFormatter(stringConverter, Configuration.instance().loadedConfig.smtp.port, integerFilter)
    }

    @FXML
    fun updateSettings() {
        Configuration.instance().loadedConfig.smtp.port = portField.text.toInt()
        println(portField.text)

        Configuration.instance().saveToUserConfig()
    }

    @FXML
    private fun startServerClick() {
        if (SmtpServer.instance().isRunning()) {
            SmtpServer.instance().stopServer()
            startButton.text = "Start server"
            statusLabel.text = "Server stopped"
        } else {
            SmtpServer.instance().startServer(portField.text.toInt())
            startButton.text = "Stop server"
            statusLabel.text = "Server started"
        }

        portField.isEditable = !SmtpServer.instance().isRunning()
    }

    fun shutdown() {
        SmtpServer.instance().stopServer()
    }
}