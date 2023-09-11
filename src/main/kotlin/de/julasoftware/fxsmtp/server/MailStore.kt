package de.julasoftware.fxsmtp.server

import de.julasoftware.fxsmtp.core.Configuration
import de.julasoftware.fxsmtp.model.Email
import org.slf4j.LoggerFactory
import org.subethamail.smtp.server.Session
import java.io.*
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.internet.MimeMessage
import kotlin.io.path.deleteIfExists
import kotlin.io.path.listDirectoryEntries


class MailStore {
    private val logger = LoggerFactory.getLogger(MailStore::class.java)

    private val dateFormat = SimpleDateFormat("ddMMyyhhmmssSSS")

    fun save(from: String?, to: String?, data: InputStream?): Email {
        val mimeMessage = MimeMessage(javax.mail.Session.getDefaultInstance(System.getProperties()), data)
        val baos = ByteArrayOutputStream()
        mimeMessage.writeTo(baos)
        val rawString = baos.toString()
        baos.close()

        return Email(
            from = from, to = to, mimeMessage = mimeMessage, filePath = saveEmailToFile(mimeMessage), rawString = rawString
        )
    }

    private fun saveEmailToFile(emailMessage: MimeMessage): Path {
        val filePath = "${Configuration.instance().loadedConfig.email.folder}${File.separator}${dateFormat.format(Date())}"

        var i = 0
        var file: File? = null
        while (file == null || file.exists()) {
            val iStr: String = if (i++ > 0) {
                i.toString()
            } else ""

            file = File("$filePath$iStr${Configuration.instance().loadedConfig.email.suffix}")
        }

        try {
            emailMessage.writeTo(FileOutputStream(file))
        } catch (e: IOException) {
            // If we can't save file, we display the error in the SMTP logs
            val smtpLogger = LoggerFactory.getLogger(Session::class.java)
            smtpLogger.error("Error: Can't save email: {}", e.message)
        }

        return file.toPath()
    }

    fun delete() {

    }

    fun deleteAll() {
        Path.of(Configuration.instance().loadedConfig.email.folder).listDirectoryEntries().forEach { it.deleteIfExists() }
    }
}