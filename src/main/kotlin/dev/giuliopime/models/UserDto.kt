package dev.giuliopime.models

import kotlinx.serialization.Serializable

// TODO: Handle camel case everywhere

@Serializable
@Suppress("PropertyName")
/**
 * Represents a user that has linked their Discord account to Todoist
 *
 * @param id: the Discord id of the user
 * @param todoist_token: token to authenticate requests to Todoist api
 */
data class UserDto(
    val id: String,
    val todoist_token: String?,
)
