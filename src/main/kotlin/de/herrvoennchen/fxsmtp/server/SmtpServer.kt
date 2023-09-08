package de.herrvoennchen.fxsmtp.server

import de.herrvoennchen.fxsmtp.BindPortException
import de.herrvoennchen.fxsmtp.OutOfRangePortException
import org.slf4j.LoggerFactory
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter
import org.subethamail.smtp.server.SMTPServer
import java.net.InetAddress

class SmtpServer {
    private val logger = LoggerFactory.getLogger(SmtpServer::class.java)
    private val smtpServer = SMTPServer(SimpleMessageListenerAdapter(MailMessageListener(MailStore())), SmtpAuthHandlerFactory())

    fun startServer(port: Int, bindAddress: InetAddress? = null) {
        try {
            smtpServer.bindAddress = bindAddress
            smtpServer.port = port
            smtpServer.start()
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

    fun isRunning() = smtpServer.isRunning

    fun stopServer() {
        if (smtpServer.isRunning) {
            logger.debug("stopping server")
            smtpServer.stop()
        }
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