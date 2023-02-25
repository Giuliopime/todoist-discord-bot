package dev.giuliopime.core.db

import dev.giuliopime.models.UserDto
import org.litote.kmongo.*

object UserDBM {
    private val col = MongoClient.database.getCollection<UserDto>("users")

    init {
        col.ensureUniqueIndex(UserDto::id)
    }

    fun create(userDto: UserDto) {
        col.save(userDto)
    }

    fun get(id: String): UserDto? {
        return col.findOne(UserDto::id eq id)
    }
}
