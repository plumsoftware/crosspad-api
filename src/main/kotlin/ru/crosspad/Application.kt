package ru.crosspad

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import ru.crosspad.features.login.configureLoginRouting
import ru.crosspad.features.note.configureNotesRouting
import ru.crosspad.features.register.configureRegisterRouting
import ru.crosspad.plugins.configureRouting
import ru.crosspad.plugins.configureSerialization

fun main() {
    Database.connect(
        "jdbc:postgresql://db:5432/crosspad",
        "org.postgresql.Driver", "postgres", "root"
    )


    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
    configureNotesRouting()
    configureRouting()
}
