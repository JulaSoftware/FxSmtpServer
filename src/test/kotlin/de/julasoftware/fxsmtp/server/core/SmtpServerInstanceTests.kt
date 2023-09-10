package de.julasoftware.fxsmtp.server.core

import de.julasoftware.fxsmtp.BindPortException
import de.julasoftware.fxsmtp.OutOfRangePortException
import de.julasoftware.fxsmtp.server.SmtpServer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SmtpServerInstanceTests {

    @Test
    fun serverStartTest() {
        Assertions.assertSame(SmtpServer.instance(), SmtpServer.instance())
    }

    @Test()
    @Throws(OutOfRangePortException::class, BindPortException::class)
    fun testPortOutOfRange() {
        assertThrows<OutOfRangePortException> {
            SmtpServer.instance().startServer(9999999)
        }
    }

    @Test
    fun testMultipleServerStop() {
        SmtpServer.instance().stopServer()
        SmtpServer.instance().stopServer()
        SmtpServer.instance().stopServer()
    }
}