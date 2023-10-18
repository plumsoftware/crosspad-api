package ru.crosspad.features.password

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
