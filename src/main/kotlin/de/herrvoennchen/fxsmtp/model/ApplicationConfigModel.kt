package de.herrvoennchen.fxsmtp.model

class ConfigModel {
    lateinit var email: EmailConfig
    lateinit var smtp: SmtpConfig
}

class EmailConfig {
    var suffix: String = ".msg"
    var folder: String = "received-emails"
}

class SmtpConfig {
    var port: Int = 9
}