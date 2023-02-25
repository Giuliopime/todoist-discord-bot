package dev.giuliopime.models.todoist.oauth

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class TokenRequestDto(
    val client_id: String,
    val client_secret: String,
    val code: String
)
