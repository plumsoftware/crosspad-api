package ru.crosspad.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.crosspad.controller.NotesController

fun Application.configureNotesRouting() {
    routing {
        post("/notes/create") {
            NotesController(call).createNote()
        }

        post("/notes/search") {
            NotesController(call).getByTitle()
        }

        get("/notes") {
            NotesController(call).getAll()
        }
    }
}