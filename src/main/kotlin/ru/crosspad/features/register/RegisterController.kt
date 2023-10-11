package ru.crosspad.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.crosspad.database.tokens.TokenDTO
import ru.crosspad.database.tokens.Tokens
import ru.crosspad.database.users.UserDTO
import ru.crosspad.database.users.Users
import ru.crosspad.utils.hashPassword
import ru.crosspad.utils.isValidEmail
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReciteRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = Users.fetchUser(registerReceiveRemote.email)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Email is already in use")
        } else {
            val token = UUID.randomUUID().toString()
            val hashedPassword = hashPassword(registerReceiveRemote.password)

            try {
                Users.insert(
                    UserDTO(
                        email = registerReceiveRemote.email,
                        password = hashedPassword
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "Email is already in use")
            }

            Tokens.insert(
                TokenDTO(
                    id = Tokens.getLastIdFromDatabase() + 1,
                    email = registerReceiveRemote.email,
                    token = token
                )
            )

            call.respond(RegisterResponseRemote(token = token))
        }
    }
}