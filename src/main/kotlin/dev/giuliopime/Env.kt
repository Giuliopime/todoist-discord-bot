package dev.giuliopime

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import mu.KotlinLogging
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.event.Level

private val log = KotlinLogging.logger {  }

object Env {
    private val dotenv: Dotenv? = try {
        dotenv()
    } catch (_: Exception) {
        log.warn(".env file not found, using System variables")
        null
    }

    var log_level: Level = Level.INFO

    var api_port: Int = 8080
    lateinit var frontend_url: String

    lateinit var mongo_connection_string: String
    lateinit var mongo_db_name: String

    lateinit var todoist_client_id: String
    lateinit var todoist_client_secret: String

    lateinit var discord_api_key: String
    var discord_activity_type: Int = Activity.ActivityType.WATCHING.key
    var discord_activity_name: String = "your tasks"
    lateinit var discord_support_server_invite_url: String

    fun loadEnv() {
        log_level = try {
            Level.valueOf(
                getStringFromEnv("log.level").uppercase()
            )
        } catch (_: IllegalArgumentException) {
            throw NoSuchElementException("Invalid LOG_LEVEL in environment")
        }

        api_port = getIntFromEnv("api.port")
        frontend_url = getStringFromEnv("frontend.url")

        mongo_connection_string = getStringFromEnv("mongo.connection.string")
        mongo_db_name = getStringFromEnv("mongo.db.name")

        todoist_client_id = getStringFromEnv("todoist.client.id")
        todoist_client_secret = getStringFromEnv("todoist.client.secret")

        discord_api_key = getStringFromEnv("discord.api.key")
        discord_activity_type = getIntFromEnv("discord.activity.type")
        discord_activity_name = getStringFromEnv("discord.activity.name")
        discord_support_server_invite_url = getStringFromEnv("discord.support.server.invite.url")
    }

    @Suppress("SameParameterValue")
    private fun getStringFromEnv(key: String) : String {
        val formattedKey = key.uppercase().replace(".", "_")
        return dotenv?.get(formattedKey)
            ?: System.getenv(formattedKey)
            ?: throw NoSuchElementException("Couldn't find any $key key in environment")
    }


    @Suppress("SameParameterValue")
    private fun getIntFromEnv(key: String) : Int = try {
        getStringFromEnv(key).toInt()
    } catch (e: NumberFormatException) {
        throw NoSuchElementException("Couldn't find any $key INTEGER key in environment")
    }

    @Suppress("SameParameterValue")
    private fun getLongFromEnv(key: String) : Long = try {
        getStringFromEnv(key).toLong()
    } catch (e: NumberFormatException) {
        throw NoSuchElementException("Couldn't find any $key LONG key in environment")
    }

    @Suppress("SameParameterValue")
    private fun getBooleanFromEnv(key: String) = getStringFromEnv(key).toBoolean()
}
