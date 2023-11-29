package ru.crosspad.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.crosspad.dto.TokenDTO


object Tokens : Table("tokens") {
    private val id = Tokens.integer("id")
    private val email = Tokens.varchar("email", 100)
    private val token = Tokens.varchar("token", 100)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.id
                it[email] = tokenDTO.email
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.selectAll().toList()
                    .map {
                        TokenDTO(
                            id = it[Tokens.id],
                            email = it[email],
                            token = it[token]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchEmailByToken(tokenSearch: String): List<TokenDTO> {
        return try {
            transaction {
                Tokens.select { token eq tokenSearch }.toList()
                    .map {
                        TokenDTO(
                            id = it[Tokens.id],
                            token = it[token],
                            email = it[email]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getLastIdFromDatabase(): Int {
        var lastId = 0

        transaction {
            val result = Tokens.slice(Tokens.id.max()).select { Tokens.id.isNotNull() }.singleOrNull()
            lastId = result?.get(Tokens.id.max()) as? Int ?: 0
        }

        return lastId
    }
}