package dev.giuliopime.api.routing.swagger

import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Route.swaggerRoutes() {
    swaggerUI(path = "swagger", swaggerFile = "openapi/openapi.yaml")
}
