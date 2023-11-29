package ru.crosspad.dto

import kotlinx.serialization.Serializable
import ru.crosspad.entity.Notes

@Serializable
data class NotesDTO(
    val noteId: Int,
    val title: String,
    val entry: String,
    val color: String,
    var email: String,
    val date: Long
)

fun CreateNoteRequest.mapToNoteDTO(): NotesDTO =
    NotesDTO(
        noteId = Notes.getLastIdFromDatabase() + 1,
        title = title,
        entry = entry,
        color = color,
        email = "",
        date = System.currentTimeMillis()
    )

fun NotesDTO.mapToCreateNoteResponse(): CreateNoteResponse =
    CreateNoteResponse(
        noteId = noteId,
        title = title,
        entry = entry,
        color = color,
        email = email,
        date = date
    )
