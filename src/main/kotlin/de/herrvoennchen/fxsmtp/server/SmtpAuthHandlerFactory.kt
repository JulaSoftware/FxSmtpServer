package de.herrvoennchen.fxsmtp.server

import org.subethamail.smtp.AuthenticationHandler
import org.subethamail.smtp.AuthenticationHandlerFactory

class SmtpAuthHandlerFactory : AuthenticationHandlerFactory {
    private val LoginMechanism = "LOGIN"

    override fun getAuthenticationMechanisms(): MutableList<String> {
        return listOf(LoginMechanism).toMutableList()
    }

    override fun create(): AuthenticationHandler {
        return SmtpAuthHandler()
    }
}