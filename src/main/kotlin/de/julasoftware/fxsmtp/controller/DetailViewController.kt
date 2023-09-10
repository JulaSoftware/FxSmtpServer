package de.julasoftware.fxsmtp.controller

import de.julasoftware.fxsmtp.core.ModelManager
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.web.WebView

class DetailViewController {

    @FXML
    private lateinit var toTextField: TextField

    @FXML
    private lateinit var fromTextField: TextField

    @FXML
    private lateinit var subjectTextField: TextField

    @FXML
    private lateinit var messageWebView: WebView

    private var toProperty = SimpleStringProperty()
    private var fromProperty = SimpleStringProperty()
    private var subjectProperty = SimpleStringProperty()
    private var messageProperty = SimpleStringProperty()

    @FXML
    fun initialize() {
        toTextField.textProperty().bindBidirectional(toProperty)
        fromTextField.textProperty().bindBidirectional(fromProperty)
        subjectTextField.textProperty().bindBidirectional(subjectProperty)

        ModelManager.instance().selectedEmailObservers.add { email ->
            toProperty.value = email?.to
            fromProperty.value = email?.from
            subjectProperty.value = email?.subjectDecoded

            messageWebView.engine.loadContent(email?.emailString)
        }
    }
}