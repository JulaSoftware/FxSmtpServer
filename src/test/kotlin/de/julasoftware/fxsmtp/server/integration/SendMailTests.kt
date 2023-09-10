package de.julasoftware.fxsmtp.server.integration

import de.julasoftware.fxsmtp.server.core.config.TEST_HOST
import de.julasoftware.fxsmtp.server.core.config.TEST_INTEGRATION_PORT
import org.apache.commons.mail.SimpleEmail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendMailTests {
    private val logger = LoggerFactory.getLogger(SendMailTests::class.java)

    @BeforeEach
    fun displayInfo() {
        logger.info("launching integration tests..")
        logger.info("You need to run the project and launch the SMTP server on port {} before testing.", TEST_INTEGRATION_PORT)
    }

    @Test
    fun sendSimpleMailTest() {
        val email = SimpleEmail()

        email.hostName = TEST_HOST
        email.setSmtpPort(TEST_INTEGRATION_PORT)
        email.setStartTLSEnabled(true)
        email.setFrom("test-sender@gmail.com")
        email.subject = "Simple Email Integration Test #1"
        email.setMsg("This is a simple email integration test. Test #1")
        email.setTo(listOf(InternetAddress("test-receiver@gmail.com")))

        email.send()
    }
}