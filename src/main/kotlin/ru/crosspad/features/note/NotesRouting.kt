package ru.crosspad.features.note

import io.ktor.server.application.*
import io.ktor.server.routing.*

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