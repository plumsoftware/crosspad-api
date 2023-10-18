package ru.crosspad.features.settings.password

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configurePasswordRouting() {
    routing {
        post("/settings/password/change") {
            val passwordController = PasswordController(call)
            passwordController.passwordChange()
        }
    }
}