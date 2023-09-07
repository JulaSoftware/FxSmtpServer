package de.herrvoennchen.fxsmtp.server

import org.subethamail.smtp.helper.SimpleMessageListener
import java.io.InputStream

class MailMessageListener(private val mailStore: MailStore) : SimpleMessageListener {
    override fun accept(from: String?, recipient: String?): Boolean {
        return true
    }

    override fun deliver(from: String?, recipient: String?, data: InputStream?) {
        mailStore.save(from, recipient, data)
    }
}