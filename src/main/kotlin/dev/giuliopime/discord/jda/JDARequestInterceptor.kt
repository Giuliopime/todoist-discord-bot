package dev.giuliopime.discord.jda

import mu.KotlinLogging
import okhttp3.Interceptor
import okhttp3.Response

private val logger = KotlinLogging.logger {  }

class JDARequestInterceptor: Interceptor {

    companion object {
        val  re1 = "interactions/ID/.*/callback".toRegex()
        val  re2 = "/webhooks/ID/.*/messages.*".toRegex()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if ("discord.com" != request.url.host) {
            return chain.proceed(request);
        }

        val response = chain.proceed(request)
        val code = response.code
        val url: String = request.url.toString()
            .replace(re1, "/interactions/ID/INTERACTION_ID/callback")
            .replace(re2, "/webhooks/ID/WEBHOOK_ID/messages")

        val method: String = request.method

        if (code == 429) {
            logger.error("Encountered rate limit, route: $method $url")
        }

        return response;
    }
}
