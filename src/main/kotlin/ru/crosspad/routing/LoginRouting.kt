package ru.crosspad.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.crosspad.controller.LoginController

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
    }
}