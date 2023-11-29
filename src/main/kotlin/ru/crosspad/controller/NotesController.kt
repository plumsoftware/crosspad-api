package ru.crosspad.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.crosspad.entity.Notes
import ru.crosspad.dto.mapToCreateNoteResponse
import ru.crosspad.dto.mapToNoteDTO
import ru.crosspad.entity.Tokens
import ru.crosspad.dto.CreateNoteRequest
import ru.crosspad.dto.SearchNoteDTO
import ru.crosspad.utils.TokenCheck

class NotesController(private val call: ApplicationCall) {

    suspend fun getByTitle() {
        val request = call.receive<SearchNoteDTO>()
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val noteDTO = Notes.fetchAllByTitle(
                request.title,
                Tokens.fetchEmailByToken(token!!).get(0).email
            )
            call.respond(noteDTO)
            return
        }

        call.respond(HttpStatusCode.Unauthorized, "Token expired")
    }

    suspend fun createNote() {
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val request = call.receive<CreateNoteRequest>()
            val note = request.mapToNoteDTO()
            note.email = Tokens.fetchEmailByToken(token!!).get(0).email
            Notes.insert(note)
            call.respond(note.mapToCreateNoteResponse())
            return
        }

        call.respond(HttpStatusCode.Unauthorized, "Token expired")
    }

    suspend fun getAll() {
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val noteDTO = Notes.fetchAllByEmail(Tokens.fetchEmailByToken(token!!).get(0).email)
            call.respond(noteDTO)
            return
        }

        call.respond(HttpStatusCode.Unauthorized, "Token expired")
    }
}