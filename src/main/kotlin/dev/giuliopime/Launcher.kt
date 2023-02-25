package dev.giuliopime

import io.ktor.server.application.Application
import dev.giuliopime.core.db.MongoClient
import ch.qos.logback.classic.Level
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.logging.*
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess
import ch.qos.logback.classic.Logger
import dev.giuliopime.api.plugins.configureMonitoring
import dev.giuliopime.api.plugins.configureSerialization
import dev.giuliopime.api.plugins.configureStatusPages
import dev.giuliopime.api.routing.configureRouting
import dev.giuliopime.discord.TodoistBot

private val log = KotlinLogging.logger {  }

suspend fun main() {
    /**
     * CONFIGURE LOGGING
     */
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = Level.convertAnSLF4JLevel(Env.log_level)

    /**
     * CONFIGURE ENVIRONMENT
     */
    try {
        Env.loadEnv()
    } catch (e: NoSuchElementException) {
        log.error(e)
        exitProcess(1)
    }

    /**
     * CONFIGURE SHUTDOWN HOOK
     */
    Runtime.getRuntime().addShutdownHook(
        Thread {
            TodoistBot.shutdown("Shutdown manually via process manager or IDE")
        }
    )

    /**
     * LAUNCH DISCORD BOT
     */
    TodoistBot.start()

    /**
     * READY TO LAUNCH API? LAUNCH!
     */
    embeddedServer(Netty, port = Env.api_port, host = "0.0.0.0", module = Application::indexApplicationModule).start(wait = true)
}

private fun Application.indexApplicationModule() {
    configureMonitoring()
    configureSerialization()
    configureStatusPages()
    configureRouting()
}
