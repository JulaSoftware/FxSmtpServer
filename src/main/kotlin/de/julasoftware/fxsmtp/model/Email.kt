package de.julasoftware.fxsmtp.model

import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.internet.MimeUtility

class Email(
    var receivedDate: Date = Date(),
    var from: String? = null,
    var to: String? = null,
    var subject: String? = null,
    var emailString: String? = null,
    var filePath: Path? = null
) {
    private val logger = LoggerFactory.getLogger(Email::class.java)
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

    val subjectDecoded: String?
        get() {
            return try {
                MimeUtility.decodeText(subject)
            } catch (ex: UnsupportedEncodingException) {
                logger.error("error decoding subject $subject", ex)
                subject
            }
        }

    val receivedDateFormatted: String
        get() = dateFormatter.format(receivedDate)
}

fun emptyEmail(): Email = Email()