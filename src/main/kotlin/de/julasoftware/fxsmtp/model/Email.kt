package de.julasoftware.fxsmtp.model

import org.apache.commons.mail.EmailAttachment
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class Email(
    var receivedDate: Date = Date(),
    var from: String? = null,
    var to: String? = null,
    var filePath: Path? = null,
    var mimeMessage: MimeMessage? = null,
    var rawString: String = ""
) {
    private val logger = LoggerFactory.getLogger(Email::class.java)
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

    val subject: String? = mimeMessage?.subject

    val cc: String? = mimeMessage?.allRecipients?.filter { it.type == Message.RecipientType.CC.toString() }?.joinToString()
    val bcc: String? = mimeMessage?.allRecipients?.filter { it.type == Message.RecipientType.BCC.toString() }?.joinToString()

    val receivedDateFormatted: String = dateFormatter.format(receivedDate)

    val mainContent: String?
        get() {
            if (mimeMessage?.content is MimeMultipart) {
                var contentPart: BodyPart? = null
                for (i in 0 until (mimeMessage!!.content as MimeMultipart).count) {
                    val part = (mimeMessage!!.content as MimeMultipart).getBodyPart(i)
                    if (part.disposition == null) {
                        if (part.contentType.contains("text/html", true)) {
                            return part.content.toString()
                        } else if (contentPart == null) {
                            contentPart = part
                        }
                    }
                }

                return contentPart?.content?.toString()
            }
            return mimeMessage?.content?.toString()
        }

    val attachments: List<BodyPart>
        get() {
            val attachmentList = mutableListOf<BodyPart>()
            if (mimeMessage?.content is MimeMultipart) {
                for (i in 0 until (mimeMessage!!.content as MimeMultipart).count) {
                    val part = (mimeMessage!!.content as MimeMultipart).getBodyPart(i)
                    if (part.disposition == EmailAttachment.ATTACHMENT) {
                        attachmentList.add(part)
                    }
                }
            }

            return attachmentList
        }
}

fun emptyEmail(): Email = Email()