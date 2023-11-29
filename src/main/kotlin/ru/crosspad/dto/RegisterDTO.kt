package ru.crosspad.dto

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
