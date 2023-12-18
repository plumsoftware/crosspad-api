package ru.crosspad.model.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.crosspad.model.dto.TokenDTO


object Token : Table("tokens") {
    private val id = Token.integer("id")
    private val email = Token.varchar("email", 100)
    private val token = Token.varchar("token", 100)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Token.insert {
                it[id] = tokenDTO.id
                it[email] = tokenDTO.email
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Token.selectAll().toList()
                    .map {
                        TokenDTO(
                            id = it[Token.id],
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
                Token.select { token eq tokenSearch }.toList()
                    .map {
                        TokenDTO(
                            id = it[Token.id],
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
            val result = Token.slice(Token.id.max()).select { Token.id.isNotNull() }.singleOrNull()
            lastId = result?.get(Token.id.max()) as? Int ?: 0
        }

        return lastId
    }
}