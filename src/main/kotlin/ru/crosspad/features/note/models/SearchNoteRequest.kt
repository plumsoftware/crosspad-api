package ru.crosspad.features.note.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchNoteRequest(
    val title: String,
)

@Serializable
data class SearchNoteResponse(
    val noteId: Int,
    val title: String,
    val entry: String,
    val color: String,
    val email: String,
    val date: String
)