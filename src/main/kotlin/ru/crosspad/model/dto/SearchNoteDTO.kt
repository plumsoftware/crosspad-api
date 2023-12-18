package ru.crosspad.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchNoteDTO(
    val title: String,
)
