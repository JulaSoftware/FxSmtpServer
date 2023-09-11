package de.julasoftware.fxsmtp.controller

import de.julasoftware.fxsmtp.core.ModelManager
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.web.WebView
import javax.mail.BodyPart

class DetailViewController {

    @FXML
    private lateinit var toTextField: TextField

    @FXML
    private lateinit var fromTextField: TextField

    @FXML
    private lateinit var ccTextField: TextField

    @FXML
    private lateinit var bccTextField: TextField

    @FXML
    private lateinit var subjectTextField: TextField

    @FXML
    private lateinit var messageWebView: WebView

    @FXML
    private lateinit var attachmentsTab: Tab

    @FXML
    private lateinit var rawMailTextArea: TextArea

    @FXML
    private lateinit var attachmentsTableView: TableView<BodyPart>

    private var toProperty = SimpleStringProperty()
    private var fromProperty = SimpleStringProperty()
    private var ccProperty = SimpleStringProperty()
    private var bccProperty = SimpleStringProperty()
    private var subjectProperty = SimpleStringProperty()
    private var rawStringProperty = SimpleStringProperty()

    @FXML
    fun initialize() {
        toTextField.textProperty().bindBidirectional(toProperty)
        fromTextField.textProperty().bindBidirectional(fromProperty)
        ccTextField.textProperty().bindBidirectional(ccProperty)
        bccTextField.textProperty().bindBidirectional(bccProperty)
        subjectTextField.textProperty().bindBidirectional(subjectProperty)
        rawMailTextArea.textProperty().bindBidirectional(rawStringProperty)

        ModelManager.instance().selectedEmailObservers.add { email ->
            toProperty.value = email?.to
            fromProperty.value = email?.from
            ccProperty.value = email?.cc
            bccProperty.value = email?.bcc
            subjectProperty.value = email?.subject
            rawStringProperty.value = email?.rawString

            messageWebView.engine.loadContent(email?.mainContent)
            attachmentsTableView.items.addAll(email?.attachments!!)
            attachmentsTab.isDisable = attachmentsTableView.items.isEmpty()
        }
    }
}