package de.julasoftware.fxsmtp.core

import kotlin.properties.Delegates

class ModelService {
    val receivedEmailObservers = mutableListOf<(String) -> Unit>()

    var newEmail: String by Delegates.observable("") { _, _, newValue ->
        receivedEmailObservers.forEach { it(newValue) }
    }

    companion object {
        @Volatile
        private var instance: ModelService? = null

        fun instance(): ModelService {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ModelService()
                    }
                }
            }

            return instance!!
        }
    }
}