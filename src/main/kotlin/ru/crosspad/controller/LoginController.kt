package ru.crosspad.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.crosspad.dto.TokenDTO
import ru.crosspad.entity.Tokens
import ru.crosspad.entity.Users
import ru.crosspad.dto.LoginReceiveRemote
import ru.crosspad.dto.LoginResponseRemote
import ru.crosspad.utils.checkPassword
import java.util.*

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.email)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
            return
        }

        if (checkPassword(receive.password, userDTO.password)) {
            val token = UUID.randomUUID().toString()
            Tokens.insert(
                TokenDTO(
                    id = Tokens.getLastIdFromDatabase() + 1,
                    email = receive.email,
                    token = token
                )
            )

            call.respond(LoginResponseRemote(token = token))
            return
        }

        call.respond(HttpStatusCode.BadRequest, "Invalid password or email")
    }
}