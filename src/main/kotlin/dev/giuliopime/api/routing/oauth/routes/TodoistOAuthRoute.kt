package dev.giuliopime.api.routing.oauth.routes

import dev.giuliopime.api.routing.frontend.FrontendRouteUris
import dev.giuliopime.api.routing.oauth.TodoistOAuthRoute
import dev.giuliopime.todoist.TodoistClient
import io.ktor.server.application.*
import io.ktor.server.resources.get
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.todoistOAuthRoute() {
    get<TodoistOAuthRoute> {
        // TODO: Check status
        val token = TodoistClient.OAuth.exchangeCodeForToken(it.code)
            ?: return@get call.respondRedirect(FrontendRouteUris.oauthFailure)

        // TODO: Store token

        call.respondRedirect(FrontendRouteUris.oauthSuccess)
    }
}
