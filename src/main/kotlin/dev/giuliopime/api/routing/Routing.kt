package dev.giuliopime.api.routing

import dev.giuliopime.api.routing.oauth.oauthRoutes
import dev.giuliopime.api.routing.swagger.swaggerRoutes
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Needed for typed queries
    install(Resources)

    routing {
        swaggerRoutes()

        oauthRoutes()
    }
}
