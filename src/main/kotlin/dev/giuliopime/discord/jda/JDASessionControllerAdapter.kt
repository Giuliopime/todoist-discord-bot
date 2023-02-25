package dev.giuliopime.discord.jda

import net.dv8tion.jda.api.utils.SessionControllerAdapter

private const val GATEWAY_URL = "wss://gateway.discord.gg"

class JDASessionControllerAdapter: SessionControllerAdapter() {
    override fun getGateway(): String {
        return GATEWAY_URL
    }
}
