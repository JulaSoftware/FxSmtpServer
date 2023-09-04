package de.herrvoennchen.fxsmtpserver

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField

class MainViewController {

    @FXML
    private lateinit var startButton: Button

    @FXML
    private lateinit var integerField: TextField

    @FXML
    private fun startServerClick() {
        println("${startButton.text} - ${integerField.text}")
    }
}