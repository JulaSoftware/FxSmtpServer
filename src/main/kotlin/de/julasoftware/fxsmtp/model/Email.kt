package de.julasoftware.fxsmtp.model

import java.nio.file.Path
import java.util.*

class Email(
    val icon: String = "\uf0e0",
    var receivedDate: Date = Date(),
    var from: String? = null,
    var to: String? = null,
    var subject: String? = null,
    var emailString: String? = null,
    var filePath: Path? = null
)

fun emptyEmail(): Email = Email()