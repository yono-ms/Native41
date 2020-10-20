package com.example.native41.network

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
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
            val (request, response, result) = Fuel.get("$url_users$login")
                .awaitStringResponseResult()
            logger.info("$request")
            logger.info("$response")
            val text = result.get()
            return json.decodeFromString(text)
        }
    }

}