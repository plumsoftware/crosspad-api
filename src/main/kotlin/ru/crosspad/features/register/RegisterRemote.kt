package ru.crosspad.features.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReciteRemote(
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponseRemote(
    val token: String
)