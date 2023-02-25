package dev.giuliopime.models.todoist.project

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class ProjectDto(
    val id: String,
    val name: String,
    val color: String,
    val parent_id: String,
    val order: Int,
    val comment_count: Int,
    val is_shared: Boolean,
    val is_favorite: Boolean,
    val is_inbox_project: Boolean,
    val is_team_project: Boolean,
    val view_style: String,
    val url: String
) {
    @Serializable
    @Suppress("PropertyName")
    data class ProjectCreateDto(
        val name: String,
        val parent_id: String?,
        val color: String?,
        val is_favorite: Boolean?,
        val view_style: String?
    )

    @Serializable
    @Suppress("PropertyName")
    data class ProjectUpdateDto(
        val name: String?,
        val color: String?,
        val is_favorite: Boolean?,
        val view_style: String?
    )
}
