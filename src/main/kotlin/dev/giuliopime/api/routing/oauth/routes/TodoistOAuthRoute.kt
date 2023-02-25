package dev.giuliopime.api.routing.oauth.routes

import dev.giuliopime.api.routing.oauth.TodoistOAuthRoute
import dev.giuliopime.todoist.TodoistClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.get
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.todoistOAuthRoute() {
    get<TodoistOAuthRoute> {
        // TODO: Check status
        val token = TodoistClient.OAuth.exchangeCodeForToken(it.code)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        // TODO: Store token associated to the Discord user

        call.respond(HttpStatusCode.OK)
    }
}
