package dev.giuliopime.models.todoist.oauth

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class TokenResponseDto(
    val access_token: String,
    val token_type: String
)
