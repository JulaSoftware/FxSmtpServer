package de.julasoftware.fxsmtp.server

import de.julasoftware.fxsmtp.BindPortException
import de.julasoftware.fxsmtp.OutOfRangePortException
import de.julasoftware.fxsmtp.core.Configuration
import de.julasoftware.fxsmtp.core.ModelManager
import de.julasoftware.fxsmtp.model.Email
import org.slf4j.LoggerFactory
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter
import org.subethamail.smtp.server.SMTPServer
import java.net.InetAddress

class SmtpServer {
    private val logger = LoggerFactory.getLogger(SmtpServer::class.java)
    private var smtpServer: SMTPServer? = null
    private val mailStore = MailStore()

    fun startServer(port: Int, bindAddress: InetAddress? = null) {
        try {
            smtpServer = SMTPServer(SimpleMessageListenerAdapter(MailMessageListener(mailStore)), SmtpAuthHandlerFactory())
            smtpServer!!.bindAddress = bindAddress
            smtpServer!!.port = port
            smtpServer!!.start()
        } catch (exception: RuntimeException) {
            if (exception.message != null && exception.message!!.contains("BindException")) { // Can't open port
                logger.error("{}. Port {}", exception.message, port)
                throw BindPortException(exception, port)
            } else if (exception.message != null && exception.message!!.contains("out of range")) { // Port out of range
                logger.error("Port {} out of range.", port)
                throw OutOfRangePortException(exception, port)
            } else { // Unknown error
                logger.error("Error on smtp start", exception)
                throw exception
            }
        }
    }

    fun isRunning() = smtpServer?.isRunning == true

    fun stopServer() {
        if (smtpServer?.isRunning == true) {
            logger.debug("stopping server")
            smtpServer!!.stop()
            smtpServer = null
        }
    }

    fun cleanUp() {
        if (Configuration.instance().loadedConfig.email.autoCleanUp) {
            mailStore.deleteAll()
        }
    }

    fun deleteMail(email: Email?) {
        mailStore.delete(email!!)
        ModelManager.instance().mailHasBeenDeleted = email
    }

    companion object {
        @Volatile
        private var instance: SmtpServer? = null

        fun instance(): SmtpServer {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SmtpServer()
                    }
                }
            }

            return instance!!
        }

    }
}