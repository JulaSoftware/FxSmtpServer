package de.herrvoennchen.fxsmtp.model

import java.nio.file.Path
import java.time.Instant
import java.util.*

class Email(
    var receivedDate: Date = Date.from(Instant.MIN),
    var from: String? = null,
    var to: String? = null,
    var subject: String? = null,
    var emailString: String? = null,
    var filePath: Path? = null
)