package de.julasoftware.fxsmtp.controller

import de.julasoftware.fxsmtp.core.Configuration
import de.julasoftware.fxsmtp.core.ModelManager
import de.julasoftware.fxsmtp.model.Email
import de.julasoftware.fxsmtp.server.SmtpServer
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.util.converter.NumberStringConverter
import org.slf4j.LoggerFactory
import org.subethamail.smtp.server.Session
import java.text.NumberFormat
import java.util.*


class MainViewController {
    private val logger = LoggerFactory.getLogger(Session::class.java)

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

    @FXML
    private lateinit var logTextArea: TextArea

    private val portProperty = SimpleIntegerProperty(Configuration.instance().loadedConfig.smtp.port)
    private val logProperty = SimpleStringProperty("")
    private val bundle = ResourceBundle.getBundle("bundles/Messages", Locale.getDefault())

    @FXML
    fun initialize() {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.isGroupingUsed = false

        val formatter = TextFormatter(NumberStringConverter(numberFormat), 0, integerFilter)
        portField.textFormatter = formatter

        formatter.valueProperty().bindBidirectional(portProperty)

        logTextArea.textProperty().bindBidirectional(logProperty)

        ModelManager.instance().receivedEmailObservers.add { newMail ->
            logger.warn("received email in Controller ${newMail.from}")
            emailTableView.items.add(newMail)
        }

        ModelManager.instance().smtpLogObservers.add { log ->
            logProperty.value += log + System.getProperty("line.separator")
        }

        ModelManager.instance().configChangedObservers.add { configModel ->
            portProperty.value = configModel.smtp.port
        }
    }

    @FXML
    fun onMouseEvent(event: MouseEvent) {
        if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
            openEmailDetails((event.source as TableView<Email>).selectionModel.selectedItem)
        }
    }

    private fun openEmailDetails(email: Email) {
        val stage = createWindowStage("detail-view.fxml", width = 600.0, height = 550.0, title = email.subject ?: "Email", alwaysOnTop = true)
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
            startButton.text = bundle.getString("mainView.startServer.label")
            statusLabel.text = bundle.getString("mainView.stopServer.text")
        } else {
            SmtpServer.instance().startServer(portField.text.toInt())
            startButton.text = bundle.getString("mainView.stopServer.label")
            statusLabel.text = bundle.getString("mainView.startServer.text")
        }

        portField.isEditable = !SmtpServer.instance().isRunning()
        settingsButton.isDisable = SmtpServer.instance().isRunning()
    }

    fun shutdown() {
        SmtpServer.instance().stopServer()
    }

    @FXML
    fun openSettingsClick() {
        val stage = createWindowStage("config-view.fxml", height = 200.0, title = bundle.getString("mainView.settings.label"), alwaysOnTop = true)
        stage.setOnHidden {
            portProperty.value = Configuration.instance().loadedConfig.smtp.port
        }
        stage.show()
    }
}