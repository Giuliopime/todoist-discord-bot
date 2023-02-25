package dev.giuliopime.discord.events

import dev.minn.jda.ktx.events.listener
import mu.KotlinLogging
import net.dv8tion.jda.api.events.ExceptionEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent
import net.dv8tion.jda.api.events.session.SessionRecreateEvent
import net.dv8tion.jda.api.events.session.SessionResumeEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent
import net.dv8tion.jda.api.sharding.ShardManager

private val logger = KotlinLogging.logger {}

object EventsListener {
    fun listen(shardManager: ShardManager) {
        shardManager.listener<GenericEvent> { event ->
            try {
                when (event) {
                    is ReadyEvent -> onReady(event)

                    is SessionResumeEvent -> onResume(event)
                    is SessionRecreateEvent -> onReconnect(event)
                    is SessionDisconnectEvent -> onDisconnect(event)
                    is ExceptionEvent -> onException(event)
                }
            }
            catch (e: Exception) {
                logger.error(e) { e.stackTraceToString() }
            }
        }
    }

    private fun onReady(event: ReadyEvent) {
        logger.info("Shard ${event.jda.shardInfo.shardId} is ready - ${event.guildAvailableCount} / ${event.guildTotalCount} guilds loaded")
    }

    private fun onResume(event: SessionResumeEvent) {
        logger.info("Shard ${event.jda.shardInfo.shardId} has resumed")
    }

    private fun onReconnect(event: SessionRecreateEvent) {
        logger.info("Shard ${event.jda.shardInfo.shardId} has reconnected")
    }

    private fun onDisconnect(event: SessionDisconnectEvent) {
        if (event.isClosedByServer) {
            logger.info(
                "Shard {} disconnected (closed by server). Code: {} {}",
                event.jda.shardInfo.shardId, event.serviceCloseFrame?.closeCode ?: -1, event.closeCode
            )
        } else {
            logger.info(
                "Shard {} disconnected. Code: {} {}",
                event.jda.shardInfo.shardId,
                event.serviceCloseFrame?.closeCode
                    ?: -1,
                event.clientCloseFrame?.closeReason ?: ""
            )
        }
    }

    private fun onException(event: ExceptionEvent) {
        if (!event.isLogged) {
            logger.error("Exception in Shard {}", event.jda.shardInfo.shardId, event.cause)
        }
    }
}
