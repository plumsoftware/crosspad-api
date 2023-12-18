package ru.crosspad.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.crosspad.model.entity.Token
import ru.crosspad.model.entity.User
import ru.crosspad.model.dto.ChangePasswordDTO
import ru.crosspad.utils.TokenCheck
import ru.crosspad.utils.checkPassword
import ru.crosspad.utils.hashPassword

class PasswordController(private val call: ApplicationCall) {

    suspend fun passwordChange() {
        val request = call.receive<ChangePasswordDTO>()
        val token = call.request.headers["Authorization"]

        if (!TokenCheck.isTokenValid(token.orEmpty())) {
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
            return
        }

        val oldPassword = request.oldPassword
        val newPassword = request.newPassword

        val email = Token.fetchEmailByToken(token!!).get(0).email
        val password = User.getPasswordByEmail(email)

        val hashedPassword = hashPassword(newPassword)
        if (!checkPassword(oldPassword, password!!)) {
            call.respond(HttpStatusCode.Unauthorized, "Passwords don't match")
            return
        }

        User.passwordChange(email, hashedPassword)
        call.respond(HttpStatusCode.OK)
    }
}
