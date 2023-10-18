package ru.crosspad.features.settings

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSettingsRouting() {
    routing {
        get("/settings") {
            val settingsController = SettingsController(call)
            settingsController.getUserData()
        }
    }
}