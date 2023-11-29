package ru.crosspad.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.crosspad.controller.PasswordController

fun Application.configurePasswordRouting() {
    routing {
        post("/settings/password/change") {
            val passwordController = PasswordController(call)
            passwordController.passwordChange()
        }
    }
}