package ru.crosspad.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchNoteDTO(
    val title: String,
)
