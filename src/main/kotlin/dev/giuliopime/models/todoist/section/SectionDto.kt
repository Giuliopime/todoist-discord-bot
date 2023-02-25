package dev.giuliopime.models.todoist.section

import kotlinx.serialization.Serializable

@Serializable
@Suppress("PropertyName")
data class SectionDto(
    val id: String,
    val project_id: String,
    val name: String,
    val order: Int,
) {
    @Serializable
    @Suppress("PropertyName")
    data class SectionCreateDto(
        val project_id: String,
        val name: String,
        val order: Int?
    )

    @Serializable
    @Suppress("PropertyName")
    data class SectionUpdateDto(
        val name: String
    )
}
