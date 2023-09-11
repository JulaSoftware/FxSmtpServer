package de.julasoftware.fxsmtp.controller

import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter
import java.text.NumberFormat
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