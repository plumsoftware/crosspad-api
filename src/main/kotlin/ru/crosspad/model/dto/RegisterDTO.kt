package ru.crosspad.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReceiveRemote(
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)
