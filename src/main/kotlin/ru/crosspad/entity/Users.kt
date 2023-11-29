package ru.crosspad.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.crosspad.dto.UserDTO

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

    fun getPasswordByEmail(emailSearch: String): String? {
        return try {
            transaction {
                Users.select { email eq emailSearch }
                    .singleOrNull()
                    ?.get(password)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun passwordChange(emailSearch: String, newPassword: String): Boolean {
        return transaction {
            val user = Users.select { email eq emailSearch }.singleOrNull()
            if (user != null) {
                Users.update({ email eq emailSearch }) {
                    it[password] = newPassword
                }
                true
            } else {
                false
            }
        }
    }
}