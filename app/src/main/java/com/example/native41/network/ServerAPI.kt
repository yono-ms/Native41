package com.example.native41.network

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class ServerAPI {

    companion object {

        private val logger = LoggerFactory.getLogger("ServerAPI")

        private const val url_users = "https://api.github.com/users/"

        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        suspend fun getUsers(login: String): UserModel {
            val text = get("$url_users$login")
            return json.decodeFromString(text)
        }

        suspend fun <T> getDecode(urlString: String, deserializer: DeserializationStrategy<T>): T {
            val text = get(urlString)
            return json.decodeFromString(deserializer, text)
        }

        suspend fun get(urlString: String): String {
            logger.info("request START")
            val (request, response, result) = Fuel.get(urlString)
                .awaitStringResponseResult()
            logger.info("$request")
            logger.info("$response")
            return result.get()
        }
    }

}