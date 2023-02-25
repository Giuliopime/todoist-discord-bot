package dev.giuliopime.models.todoist.project

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class CollaboratorDto(
    val id: String,
    val name: String,
    val email: String
)
