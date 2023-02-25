package dev.giuliopime.api.plugins

import dev.giuliopime.Env
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Env.log_level
    }
}
