package de.julasoftware.fxsmtp.log

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import de.julasoftware.fxsmtp.core.ModelManager

class SmtpLogAppender : AppenderBase<ILoggingEvent>() {
    override fun append(eventObject: ILoggingEvent?) {
        if (eventObject != null) {
            ModelManager.instance().newLogMessage = eventObject.formattedMessage
        }
    }
}