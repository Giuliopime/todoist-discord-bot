package dev.giuliopime.api.routing.oauth

import dev.giuliopime.api.routing.oauth.routes.todoistOAuthRoute
import io.ktor.resources.*
import io.ktor.server.routing.*

@Resource("/todoist-oauth")
class TodoistOAuthRoute(val code: String, val state: String)

fun Route.oauthRoutes() {
    todoistOAuthRoute()
}
