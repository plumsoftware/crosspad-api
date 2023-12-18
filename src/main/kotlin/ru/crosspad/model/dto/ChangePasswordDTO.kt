package ru.crosspad.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDTO(
    val oldPassword: String,
    val newPassword: String
)
