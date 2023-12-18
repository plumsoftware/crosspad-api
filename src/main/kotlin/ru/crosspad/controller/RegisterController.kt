package ru.crosspad.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.crosspad.model.dto.TokenDTO
import ru.crosspad.model.entity.Token
import ru.crosspad.model.dto.UserDTO
import ru.crosspad.model.entity.User
import ru.crosspad.model.dto.RegisterReceiveRemote
import ru.crosspad.model.dto.RegisterResponseRemote
import ru.crosspad.utils.hashPassword
import ru.crosspad.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = User.fetchUser(registerReceiveRemote.email)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Email is already in use")
            return
        }

        val token = UUID.randomUUID().toString()
        val hashedPassword = hashPassword(registerReceiveRemote.password)

        try {
            User.insert(
                UserDTO(
                    email = registerReceiveRemote.email,
                    password = hashedPassword
                )
            )
        } catch (e: ExposedSQLException) {
            call.respond(HttpStatusCode.Conflict, "Email is already in use")
        }

        Token.insert(
            TokenDTO(
                id = Token.getLastIdFromDatabase() + 1,
                email = registerReceiveRemote.email,
                token = token
            )
        )

        call.respond(RegisterResponseRemote(token = token))

    }
}