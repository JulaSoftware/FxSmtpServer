package de.herrvoennchen.fxsmtp.controller

import de.herrvoennchen.fxsmtp.core.Configuration
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.stage.Stage
import javafx.util.StringConverter
import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat


class ConfigViewController {

    @FXML
    private lateinit var portField: TextField

    @FXML
    private lateinit var emailFolderField: TextField

    private val portProperty = SimpleIntegerProperty(Configuration.instance().loadedConfig.smtp.port)
    private val emailFolderProperty = SimpleStringProperty(Configuration.instance().loadedConfig.email.folder)

    private fun initBinding() {
        portProperty.addListener { _, _, newValue -> Configuration.instance().loadedConfig.smtp.port = newValue.toInt() }
        emailFolderProperty.addListener { _, _, newValue -> Configuration.instance().loadedConfig.email.folder = newValue }
    }

    @FXML
    fun initialize() {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.isGroupingUsed = false

        val converter: StringConverter<Number> = NumberStringConverter(numberFormat)
        val formatter = TextFormatter(converter, Configuration.instance().loadedConfig.smtp.port, integerFilter)
        portField.textFormatter = formatter
        formatter.valueProperty().bindBidirectional(portProperty)

        emailFolderField.textProperty().bindBidirectional(emailFolderProperty)

        initBinding()
    }

    fun cancelWindow(actionEvent: ActionEvent) {
        if (actionEvent.target is Button) {
            if ((actionEvent.target as Button).isDefaultButton) {
                Configuration.instance().saveToUserConfig()
            }

            ((actionEvent.target as Button).scene.window as Stage).close()
        }
    }
}