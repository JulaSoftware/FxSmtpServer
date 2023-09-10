package de.julasoftware.fxsmtp.model

import java.nio.file.Path
import java.util.*

class Email(
    var receivedDate: Date = Date(),
    var from: String? = null,
    var to: String? = null,
    var subject: String? = null,
    var emailString: String? = null,
    var filePath: Path? = null
)