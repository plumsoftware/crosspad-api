package ru.crosspad.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.crosspad.database.tokens.TokenDTO
import ru.crosspad.database.tokens.Tokens
import ru.crosspad.database.users.Users
import ru.crosspad.utils.checkPassword
import java.util.*

class LoginController(private val call: ApplicationCall) {

    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.email)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
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
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password or email")
            }
        }
    }
}