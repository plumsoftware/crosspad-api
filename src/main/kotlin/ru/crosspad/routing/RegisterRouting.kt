package ru.crosspad.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.crosspad.controller.RegisterController

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()
        }
    }
}