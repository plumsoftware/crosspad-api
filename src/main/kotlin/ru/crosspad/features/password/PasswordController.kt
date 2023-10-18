package ru.crosspad.features.password

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.crosspad.database.tokens.Tokens
import ru.crosspad.database.users.Users
import ru.crosspad.utils.TokenCheck
import ru.crosspad.utils.checkPassword
import ru.crosspad.utils.hashPassword

class PasswordController(private val call: ApplicationCall) {

    suspend fun passwordChange() {
        val request = call.receive<ChangePasswordRequest>()
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val oldPassword = request.oldPassword
            val newPassword = request.newPassword

            val email = Tokens.fetchEmailByToken(token!!).get(0).email
            val password = Users.getPasswordByEmail(email)

            val hashedPassword = hashPassword(newPassword)
            if (checkPassword(oldPassword, password!!)) {
                Users.passwordChange(email, hashedPassword)

                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Passwords don't match")
            }

        } else {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }
}
