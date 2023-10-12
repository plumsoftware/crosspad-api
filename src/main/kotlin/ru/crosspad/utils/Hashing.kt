package ru.crosspad.utils

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password: String): String {
    val salt = BCrypt.gensalt()
    return BCrypt.hashpw(password, salt)
}

fun checkPassword(inputPassword: String, hashedPassword: String): Boolean {
    return BCrypt.checkpw(inputPassword, hashedPassword)
}