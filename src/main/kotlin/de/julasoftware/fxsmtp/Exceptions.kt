package de.julasoftware.fxsmtp

import org.slf4j.LoggerFactory

abstract class AbstractPortException(exception: Exception, val port: Int) : Exception(exception)

class BindPortException(exception: Exception, port: Int) : AbstractPortException(exception, port)

class InvalidPortException(exception: Exception, port: Int) : AbstractPortException(exception, port)

class OutOfRangePortException(exception: Exception, port: Int) : AbstractPortException(exception, port)

class UncaughtExceptionHandler(val component: Any) : Thread.UncaughtExceptionHandler {
    private val logger = LoggerFactory.getLogger(UncaughtExceptionHandler::class.java)
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        logger.error("${t.toString()} - ${e?.message}", e)
    }
}