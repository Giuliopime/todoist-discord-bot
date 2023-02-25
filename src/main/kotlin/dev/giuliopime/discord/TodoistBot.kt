package dev.giuliopime.discord

import dev.giuliopime.Env
import dev.giuliopime.core.db.MongoClient
import dev.giuliopime.discord.events.EventsListener
import dev.giuliopime.discord.jda.JDARequestInterceptor
import dev.giuliopime.discord.jda.JDASessionControllerAdapter
import dev.minn.jda.ktx.jdabuilder.injectKTX
import mu.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.internal.utils.IOUtil
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger { }

object TodoistBot {
    var launchTimestamp: Long = 0
    lateinit var shardManager: ShardManager

    suspend fun start() {
        launchTimestamp = System.currentTimeMillis()

        println("Starting Todoist Discord bot")

        try {
            shardManager = DefaultShardManagerBuilder.createLight(Env.discord_api_key)
                .setSessionController(JDASessionControllerAdapter())
                .setHttpClient(
                    IOUtil.newHttpClientBuilder()
                        .addInterceptor(JDARequestInterceptor())
                        .build()
                )
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.of(
                    Activity.ActivityType.fromKey(Env.discord_activity_type),
                    Env.discord_activity_name
                ))
                .injectKTX()
                .build()

            RestAction.setDefaultTimeout(60000, TimeUnit.MILLISECONDS)

            EventsListener.listen(shardManager)

            shardManager.shards.lastOrNull()?.awaitReady()
        } catch (e: Exception) {
            logger.error("An error occurred while starting Todoist Discord bot!", e)

            shutdown("ShardsBuilder exception was thrown")
        }
    }

    fun shutdown(reason: String) {
        logger.warn("Shutting down, reason: $reason")

        shardManager.shutdown()

        var counter = 0
        fixedRateTimer("Shutdown tryharder", false, 0L, 10000) {
            counter++

            if (shardManager.statuses.all { it.value == JDA.Status.SHUTDOWN }) {
                logger.info("All shards have been shutdown")

                MongoClient.close()

                exitProcess(0)
            }

            if (counter >= 60) {
                logger.error("Couldn't shutdown the shards manager properly, it took over 10 minutes and it still hasn't finished")
                MongoClient.close()
                exitProcess(1)
            }
        }
    }
}
