package ru.crosspad.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.crosspad.controller.SettingsController

fun Application.configureSettingsRouting() {
    routing {
        get("/settings") {
            val settingsController = SettingsController(call)
            settingsController.getUserData()
        }
    }
}