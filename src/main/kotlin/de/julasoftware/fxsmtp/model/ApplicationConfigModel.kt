package de.julasoftware.fxsmtp.model

class ConfigModel {
    lateinit var email: EmailConfig
    lateinit var smtp: SmtpConfig
}

class EmailConfig {
    var suffix: String = ".msg"
    var folder: String = "received-emails"
    var autoCleanUp: Boolean = true
}

class SmtpConfig {
    var port: Int = 9
}