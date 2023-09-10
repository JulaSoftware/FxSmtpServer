package de.julasoftware.fxsmtp.controller

import de.julasoftware.fxsmtp.FxSmtpServerApplication
import de.julasoftware.fxsmtp.core.Configuration
import de.julasoftware.fxsmtp.core.ModelManager
import de.julasoftware.fxsmtp.model.Email
import de.julasoftware.fxsmtp.server.SmtpServer
import javafx.beans.property.SimpleIntegerProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.util.converter.NumberStringConverter
import org.slf4j.LoggerFactory
import java.text.NumberFormat


class MainViewController {
    private val logger = LoggerFactory.getLogger(MainViewController::class.java)

    @FXML
    private lateinit var startButton: Button

    @FXML
    private lateinit var settingsButton: Button

    @FXML
    private lateinit var portField: TextField

    @FXML
    private lateinit var statusLabel: Label

    @FXML
    private lateinit var emailTableView: TableView<Email>

    private val portProperty = SimpleIntegerProperty(Configuration.instance().loadedConfig.smtp.port)

    @FXML
    fun initialize() {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.isGroupingUsed = false

        val formatter = TextFormatter(NumberStringConverter(numberFormat), 0, integerFilter)
        portField.textFormatter = formatter

        formatter.valueProperty().bindBidirectional(portProperty)

        ModelManager.instance().receivedEmailObservers.add { newMail ->
            logger.warn("received email in Controller ${newMail.from}")
            emailTableView.items.add(newMail)
        }
    }

    @FXML
    fun onMouseEvent(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
            openEmailDetails((event.source as TableView<Email>).selectionModel.selectedItem)
        }
    }

    private fun openEmailDetails(email: Email) {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(FxSmtpServerApplication::class.java.getResource("detail-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 500.0)
        stage.title = email.subject
        stage.scene = scene
        stage.isAlwaysOnTop = true
        stage.show()

        ModelManager.instance().selectedEmail = email
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

    @FXML
    fun openSettingsClick() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(FxSmtpServerApplication::class.java.getResource("config-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 400.0, 200.0)
        stage.title = "FxSMTP Server - Einstellungen"
        stage.scene = scene
        stage.isAlwaysOnTop = true
        stage.setOnHidden {
            portProperty.value = Configuration.instance().loadedConfig.smtp.port
        }
        stage.show()
    }
}