package de.julasoftware.fxsmtp.controller

import de.julasoftware.fxsmtp.core.Configuration
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat


class ConfigViewController {

    @FXML
    private lateinit var portField: TextField

    @FXML
    private lateinit var emailFolderField: TextField

    @FXML
    private lateinit var emailExtensionComboBox: ComboBox<String>

    @FXML
    private lateinit var autoCleanUpCheckBox: CheckBox

    private val portProperty = SimpleIntegerProperty(Configuration.instance().loadedConfig.smtp.port)
    private val emailFolderProperty = SimpleStringProperty(Configuration.instance().loadedConfig.email.folder)
    private val emailExtensionProperty = SimpleStringProperty(Configuration.instance().loadedConfig.email.suffix)
    private val autoCleanUpProperty = SimpleBooleanProperty(Configuration.instance().loadedConfig.email.autoCleanUp)

    private fun initBinding() {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.isGroupingUsed = false

        val formatter = TextFormatter(NumberStringConverter(numberFormat), Configuration.instance().loadedConfig.smtp.port, integerFilter)
        portField.textFormatter = formatter
        formatter.valueProperty().bindBidirectional(portProperty)
        portProperty.addListener { _, _, newValue -> Configuration.instance().loadedConfig.smtp.port = newValue.toInt() }

        emailFolderField.textProperty().bindBidirectional(emailFolderProperty)
        emailFolderProperty.addListener { _, _, newValue -> Configuration.instance().loadedConfig.email.folder = newValue }

        emailExtensionComboBox.valueProperty().bindBidirectional(emailExtensionProperty)
        emailExtensionProperty.addListener { _, _, newValue -> Configuration.instance().loadedConfig.email.suffix = newValue }

        autoCleanUpCheckBox.selectedProperty().bindBidirectional(autoCleanUpProperty)
        autoCleanUpProperty.addListener { _, _, newValue -> Configuration.instance().loadedConfig.email.autoCleanUp = newValue }
    }

    @FXML
    fun initialize() {
        emailExtensionComboBox.items.addAll(".eml", ".msg")

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