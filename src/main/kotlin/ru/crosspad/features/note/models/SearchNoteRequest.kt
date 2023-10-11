package ru.crosspad.features.note.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchNoteRequest(
    val title: String,
)
