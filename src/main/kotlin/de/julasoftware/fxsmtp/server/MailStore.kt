package de.julasoftware.fxsmtp.server

import de.julasoftware.fxsmtp.core.Configuration
import de.julasoftware.fxsmtp.core.I18n
import de.julasoftware.fxsmtp.model.Email
import org.slf4j.LoggerFactory
import org.subethamail.smtp.server.Session
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.internet.MimeMessage


class MailStore {
    private val logger = LoggerFactory.getLogger(MailStore::class.java)
    private val lineSeparator = System.getProperty("line.separator")

    // This can be a static variable since it is Thread Safe
    private val subjectPattern: Pattern = Pattern.compile("^Subject: (.*)$")

    private val dateFormat = SimpleDateFormat("ddMMyyhhmmssSSS")


    fun save(from: String?, to: String?, data: InputStream?): Email {
        val mimeMessage = MimeMessage(javax.mail.Session.getDefaultInstance(System.getProperties()), data)
        return Email(
            from = from,
            to = to,
            emailString = mimeMessage.content.toString(),
            subject = mimeMessage.subject,
            filePath = saveEmailToFile(mimeMessage)
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
            //Files.writeString(file.toPath(), emailObj)
        } catch (e: IOException) {
            // If we can't save file, we display the error in the SMTP logs
            val smtpLogger = LoggerFactory.getLogger(Session::class.java)
            smtpLogger.error("Error: Can't save email: {}", e.message)
        }

        return file.toPath()
    }

    private fun convertToString(data: InputStream?): String {
        if (data != null) {
            val lineNbToStartCopy: Long = 4 // Do not copy the first 4 lines (received part)

            val reader = BufferedReader(InputStreamReader(data, Charset.forName(I18n.UTF8)))
            val sb = StringBuilder()

            var line: String?
            var lineNb: Long = 0
            try {
                while (reader.readLine().also { line = it } != null) {
                    if (++lineNb > lineNbToStartCopy) {
                        sb.append(line).append(lineSeparator)
                    }
                }
            } catch (e: IOException) {
                logger.error("", e)
            }
            return sb.toString()
        }

        return ""
    }

    private fun getSubjectFromEmail(data: String?): String {
        if (data != null) {
            try {
                val reader = BufferedReader(StringReader(data))
                var line: String
                while (reader.readLine().also { line = it } != null) {
                    val matcher: Matcher = subjectPattern.matcher(line)
                    if (matcher.matches()) {
                        return matcher.group(1)
                    }
                }
            } catch (e: IOException) {
                logger.error("error reading subject from Mail", e)
            }
        }

        return ""
    }

    fun delete() {

    }
}