package dev.giuliopime.api.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.logging.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Exception> { call, cause ->
            call.application.environment.log.error(cause)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}
