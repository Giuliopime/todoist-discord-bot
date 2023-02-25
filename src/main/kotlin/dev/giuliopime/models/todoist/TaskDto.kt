package dev.giuliopime.models.todoist

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
@Suppress("PropertyName")
data class TaskDto(
    val id: String,
    val project_id: String,
    val section_id: String?,
    val content: String,
    val description: String?,
    val is_completed: Boolean,
    val labels: List<String>,
    val parent_id: String?,
    val order: Int,
    val priority: Int,
    val due: DueDate,
    val url: String,
    val comment_count: Int,
    val created_at: String,
    val creator_id: String,
    val assignee_id: String?,
    val assigner_id: String?
) {
    @Serializable
    @Suppress("PropertyName")
    data class TaskCreateDto(
        val project_id: String?,
        val section_id: String?,
        val content: String,
        val description: String?,
        val labels: List<String>?,
        val parent_id: String?,
        val priority: Int?,
        val due_string: String?,
        val due_date: String?,
        val due_datetime: String?,
        val due_lang: String?,
        val assignee_id: String?,
    )

    @Serializable
    @Suppress("PropertyName")
    data class TaskUpdateDto(
        val content: String?,
        val description: String?,
        val labels: List<String>?,
        val priority: Int?,
        val due_string: String?,
        val due_date: String?,
        val due_datetime: String?,
        val due_lang: String?,
        val assignee_id: String?,
    )

    @Serializable
    @Suppress("PropertyName")
    data class DueDate(
        val date: String,
        val is_recurring: Boolean,
        val datetime: String,
        val string: String,
        val timezone: String
    )
}

