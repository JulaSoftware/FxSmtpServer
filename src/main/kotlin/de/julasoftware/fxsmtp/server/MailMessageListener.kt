package de.julasoftware.fxsmtp.server

import de.julasoftware.fxsmtp.core.ModelService
import org.slf4j.LoggerFactory
import org.subethamail.smtp.helper.SimpleMessageListener
import java.io.InputStream

class MailMessageListener(private val mailStore: MailStore) : SimpleMessageListener {
    private val logger = LoggerFactory.getLogger(MailMessageListener::class.java)

    override fun accept(from: String?, recipient: String?): Boolean {
        return true
    }

    override fun deliver(from: String?, recipient: String?, data: InputStream?) {
        ModelService.instance().newEmail = from!!
        mailStore.save(from, recipient, data)
    }
}