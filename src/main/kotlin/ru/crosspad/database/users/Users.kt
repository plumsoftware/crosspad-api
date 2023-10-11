package ru.crosspad.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    private val email = Users.varchar("email", 100)
    private val password = Users.varchar("password", 100)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[email] = userDTO.email
                it[password] = userDTO.password
            }
        }
    }

    fun fetchUser(email: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select { Users.email.eq(email) }.single()
                UserDTO(
                    email = userModel[Users.email],
                    password = userModel[password]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}