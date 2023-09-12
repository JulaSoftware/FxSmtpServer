package de.julasoftware.fxsmtp.core

import de.julasoftware.fxsmtp.model.ConfigModel
import de.julasoftware.fxsmtp.model.Email
import de.julasoftware.fxsmtp.model.emptyEmail
import kotlin.properties.Delegates

class ModelManager {
    val receivedEmailObservers = mutableListOf<(Email) -> Unit>()
    val selectedEmailObservers = mutableListOf<(Email?) -> Unit>()
    val smtpLogObservers = mutableListOf<(String) -> Unit>()
    val configChangedObservers = mutableListOf<(ConfigModel) -> Unit>()
    val emailDeleteObservers = mutableListOf<(Email?) -> Unit>()

    var newEmail: Email by Delegates.observable(emptyEmail()) { _, _, newValue ->
        receivedEmailObservers.forEach { it(newValue) }
    }

    var selectedEmail: Email? by Delegates.observable(null) { _, _, newValue ->
        selectedEmailObservers.forEach { it(newValue) }
    }

    var newLogMessage: String by Delegates.observable("") { _, _, newValue ->
        smtpLogObservers.forEach { it(newValue) }
    }

    var currentConfig: ConfigModel by Delegates.observable(ConfigModel()) { _, _, newValue ->
        configChangedObservers.forEach { it(newValue) }
    }

    var mailHasBeenDeleted: Email? by Delegates.observable(null) { _, _, newValue ->
        emailDeleteObservers.forEach { it(newValue) }
    }

    companion object {
        @Volatile
        private var instance: ModelManager? = null

        fun instance(): ModelManager {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ModelManager()
                    }
                }
            }

            return instance!!
        }
    }
}