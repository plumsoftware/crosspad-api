package ru.crosspad.utils

import ru.crosspad.model.entity.Token

object TokenCheck {

    fun isTokenValid(token: String): Boolean = Token.fetchTokens().firstOrNull { it.token == token } != null

}