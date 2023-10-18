package ru.crosspad.features.password

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configurePasswordRouting() {
    routing {
        post("/password/change") {
            val passwordController = PasswordController(call)
            passwordController.passwordChange()
        }
    }
}