package de.herrvoennchen.fxsmtp.server

import org.subethamail.smtp.AuthenticationHandler

class SmtpAuthHandler : AuthenticationHandler {
    private val UserIdentity = "User"
    private val PromptUserName = "334 VXNlcm5hbWU6" // VXNlcm5hbWU6 is base64 for "Username:"
    private val PromptPassword = "334 UGFzc3dvcmQ6" // UGFzc3dvcmQ6 is base64 for "Password:"

    private var pass = 0

    override fun auth(clientInput: String?): String? {
        val prompt: String?

        if (++pass == 1) {
            prompt = PromptUserName
        } else if (pass == 2) {
            prompt = PromptPassword
        } else {
            pass = 0
            prompt = null
        }

        return prompt
    }

    override fun getIdentity(): Any {
        return UserIdentity;
    }
}