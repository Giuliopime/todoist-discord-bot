package dev.giuliopime.todoist

import dev.giuliopime.Env
import dev.giuliopime.core.exceptions.UnknownTodoistException
import dev.giuliopime.models.todoist.TaskDto
import dev.giuliopime.models.todoist.oauth.TokenRequestDto
import dev.giuliopime.models.todoist.oauth.TokenResponseDto
import dev.giuliopime.models.todoist.project.CollaboratorDto
import dev.giuliopime.models.todoist.project.ProjectDto
import dev.giuliopime.models.todoist.section.SectionDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object TodoistClient {
    private val httpClient = HttpClient(Apache) {
        install(Logging)
        install(ContentNegotiation) {
            json(Json)
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
        defaultRequest {
            url("https://api.todoist.com/rest/v2/")
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        expectSuccess = false
    }

    object OAuth {
        suspend fun exchangeCodeForToken(code: String): String? {
            val response: HttpResponse = httpClient.post("https://todoist.com/oauth/access_token") {
                setBody(TokenRequestDto(
                    client_id = Env.todoist_client_id,
                    client_secret = Env.todoist_client_secret,
                    code = code
                ))
            }

            return if (response.status.isSuccess())
                response.body<TokenResponseDto>().access_token
            else {
                logger.warn("Received status code ${response.status} from https://todoist.com/oauth/access_token")
                null
            }
        }
    }

    object Projects {
        suspend fun getAllProjects(token: String): List<ProjectDto>? {
            val response = httpClient.get("project") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                response.body<List<ProjectDto>>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching all projects", response.status.value)
        }

        suspend fun createProject(token: String, projectCreateDto: ProjectDto.ProjectCreateDto): ProjectDto? {
            val response = httpClient.post("projects") {
                header("Authorization", "Bearer $token")
                setBody(projectCreateDto)
            }

            return if (response.status.isSuccess())
                response.body<ProjectDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed creating project", response.status.value)
        }

        suspend fun getProject(token: String, id: String): ProjectDto? {
            val response = httpClient.get("projects/$id") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                response.body<ProjectDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching project", response.status.value)
        }

        suspend fun updateProject(token: String, id: String, projectUpdateDto: ProjectDto.ProjectUpdateDto): ProjectDto? {
            val response = httpClient.post("projects/$id") {
                header("Authorization", "Bearer $token")
                setBody(projectUpdateDto)
            }

            return if (response.status.isSuccess())
                response.body<ProjectDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed updating project", response.status.value)
        }

        suspend fun deleteProject(token: String, id: String): Boolean {
            val response = httpClient.delete("projects/$id") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                true
            else if (response.status.value == 401)
                false
            else
                throw UnknownTodoistException("Failed deleting project", response.status.value)
        }

        suspend fun getCollaborators(token: String, projectId: String): List<CollaboratorDto>? {
            val response = httpClient.get("projects/$projectId/collaborators") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                response.body<List<CollaboratorDto>>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching all collaborators", response.status.value)
        }
    }

    object Sections {
        suspend fun getAllSections(token: String, projectId: String?): List<SectionDto>? {
            val response = httpClient.get("sections") {
                header("Authorization", "Bearer $token")
                parameter("project_id", projectId)
            }

            return if (response.status.isSuccess())
                response.body<List<SectionDto>>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching all sections", response.status.value)
        }

        suspend fun createSection(token: String, sectionCreateDto: SectionDto.SectionCreateDto): SectionDto? {
            val response = httpClient.post("sections") {
                header("Authorization", "Bearer $token")
                setBody(sectionCreateDto)
            }

            return if (response.status.isSuccess())
                response.body<SectionDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed creating section", response.status.value)
        }

        suspend fun getSection(token: String, id: String): ProjectDto? {
            val response = httpClient.get("sections/$id") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                response.body<ProjectDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching section", response.status.value)
        }

        suspend fun updateSection(token: String, id: String, sectionUpdateDto: ProjectDto.ProjectUpdateDto): SectionDto? {
            val response = httpClient.post("sections/$id") {
                header("Authorization", "Bearer $token")
                setBody(sectionUpdateDto)
            }

            return if (response.status.isSuccess())
                response.body<SectionDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed updating section", response.status.value)
        }

        suspend fun deleteSection(token: String, id: String): Boolean {
            val response = httpClient.delete("sections/$id") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                true
            else if (response.status.value == 401)
                false
            else
                throw UnknownTodoistException("Failed deleting section", response.status.value)
        }
    }

    object Tasks {
        suspend fun getActiveTasks(
            token: String,
            projectId: String? = null,
            sectionId: String? = null,
            label: String? = null,
            filter: String? = null,
            ids: List<String>? = null,
        ): List<TaskDto>? {
            val response = httpClient.get("tasks") {
                header("Authorization", "Bearer $token")
                parameter("project_id", projectId)
                parameter("section_id", sectionId)
                parameter("label", label)
                parameter("filter", filter)
                parameter("ids", ids)
            }

            return if (response.status.isSuccess())
                response.body<List<TaskDto>>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching all tasks", response.status.value)
        }

        suspend fun createTask(token: String, taskCreateDto: TaskDto.TaskCreateDto): TaskDto? {
            val response = httpClient.post("tasks") {
                header("Authorization", "Bearer $token")
                setBody(taskCreateDto)
            }

            return if (response.status.isSuccess())
                response.body<TaskDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed creating task", response.status.value)
        }

        suspend fun getActiveTask(token: String, id: String): TaskDto? {
            val response = httpClient.get("tasks/$id") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                response.body<TaskDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed fetching task", response.status.value)
        }

        suspend fun updateTask(token: String, id: String, taskUpdateDto: TaskDto.TaskUpdateDto): TaskDto? {
            val response = httpClient.post("tasks/$id") {
                header("Authorization", "Bearer $token")
                setBody(taskUpdateDto)
            }

            return if (response.status.isSuccess())
                response.body<TaskDto>()
            else if (response.status.value == 401)
                null
            else
                throw UnknownTodoistException("Failed updating task", response.status.value)
        }

        suspend fun closeTask(token: String, id: String): Boolean {
            val response = httpClient.delete("tasks/$id/close") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                true
            else if (response.status.value == 401)
                false
            else
                throw UnknownTodoistException("Failed deleting task", response.status.value)
        }

        suspend fun reopenTask(token: String, id: String): Boolean {
            val response = httpClient.get("tasks/$id/reopen") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                true
            else if (response.status.value == 401)
                false
            else
                throw UnknownTodoistException("Failed reopening task", response.status.value)
        }

        suspend fun deleteTask(token: String, id: String): Boolean {
            val response = httpClient.delete("tasks/$id") {
                header("Authorization", "Bearer $token")
            }

            return if (response.status.isSuccess())
                true
            else if (response.status.value == 401)
                false
            else
                throw UnknownTodoistException("Failed deleting task", response.status.value)
        }
    }

    // TODO
    object Comments

    // TODO
    object Labels
}
