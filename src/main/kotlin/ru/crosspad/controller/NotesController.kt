package ru.crosspad.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.crosspad.model.entity.Note
import ru.crosspad.model.dto.mapToCreateNoteResponse
import ru.crosspad.model.dto.mapToNoteDTO
import ru.crosspad.model.entity.Token
import ru.crosspad.model.dto.CreateNoteRequest
import ru.crosspad.model.dto.SearchNoteDTO
import ru.crosspad.utils.TokenCheck

class NotesController(private val call: ApplicationCall) {

    suspend fun getByTitle() {
        val request = call.receive<SearchNoteDTO>()
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val noteDTO = Note.fetchAllByTitle(
                request.title,
                Token.fetchEmailByToken(token!!).get(0).email
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
            note.email = Token.fetchEmailByToken(token!!).get(0).email
            Note.insert(note)
            call.respond(note.mapToCreateNoteResponse())
            return
        }

        call.respond(HttpStatusCode.Unauthorized, "Token expired")
    }

    suspend fun getAll() {
        val token = call.request.headers["Authorization"]

        if (TokenCheck.isTokenValid(token.orEmpty())) {
            val noteDTO = Note.fetchAllByEmail(Token.fetchEmailByToken(token!!).get(0).email)
            call.respond(noteDTO)
            return
        }

        call.respond(HttpStatusCode.Unauthorized, "Token expired")
    }
}