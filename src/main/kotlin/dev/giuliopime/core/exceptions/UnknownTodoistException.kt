package dev.giuliopime.core.exceptions

class UnknownTodoistException(
    override val message: String,
    statusCode: Int?
) : RuntimeException(
    message +
            "\nStatus code: ${statusCode ?: ""}"
)
