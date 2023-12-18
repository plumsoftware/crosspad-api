package ru.crosspad.model.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.crosspad.model.dto.UserDTO

object User : Table("users") {
    private val email = User.varchar("email", 100)
    private val password = User.varchar("password", 100)

    fun insert(userDTO: UserDTO) {
        transaction {
            User.insert {
                it[email] = userDTO.email
                it[password] = userDTO.password
            }
        }
    }

    fun fetchUser(email: String): UserDTO? {
        return try {
            transaction {
                val userModel = User.select { User.email.eq(email) }.single()
                UserDTO(
                    email = userModel[User.email],
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
                User.select { email eq emailSearch }
                    .singleOrNull()
                    ?.get(password)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun passwordChange(emailSearch: String, newPassword: String): Boolean {
        return transaction {
            val user = User.select { email eq emailSearch }.singleOrNull()
            if (user != null) {
                User.update({ email eq emailSearch }) {
                    it[password] = newPassword
                }
                true
            } else {
                false
            }
        }
    }
}