package ru.crosspad.features.note.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateNoteRequest(
    val title: String,
    val entry: String,
    val color: String,
)

@Serializable
data class CreateNoteResponse(
    val noteId: Int,
    val title: String,
    val entry: String,
    val color: String,
    val email: String,
    val date: Long
)