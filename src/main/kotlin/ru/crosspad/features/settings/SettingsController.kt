package ru.crosspad.features.settings

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.crosspad.database.tokens.Tokens
import ru.crosspad.utils.TokenCheck

class SettingsController(private val call: ApplicationCall) {

    suspend fun getUserData() {
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val email = Tokens.fetchEmailByToken(token!!).get(0).email
            call.respond(email)
            return
        }

        call.respond(HttpStatusCode.Unauthorized, "Token expired")
    }
}