package ru.crosspad.database.notes

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Notes : Table("notes") {
    private val noteId = Notes.integer("id")
    private val title = Notes.varchar("title", 50)
    private val entry = Notes.varchar("entry", 3000)
    private val color = Notes.varchar("color", 20)
    private val email = Notes.varchar("email", 100)
    private val date = Notes.long("date")

    fun insert(notesDTO: NotesDTO) {
        transaction {
            Notes.insert {
                it[noteId] = notesDTO.noteId
                it[title] = notesDTO.title
                it[entry] = notesDTO.entry
                it[color] = notesDTO.color
                it[email] = notesDTO.email
                it[date] = notesDTO.date
            }
        }
    }

    fun fetchAll(): List<NotesDTO> {
        return try {
            transaction {
                Notes.selectAll().toList()
                    .map {
                        NotesDTO(
                            noteId = it[noteId],
                            title = it[title],
                            entry = it[entry],
                            color = it[color],
                            email = it[email],
                            date = it[date]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchAllByEmail(emailSearch: String): List<NotesDTO> {
        return try {
            transaction {
                Notes.select { Notes.email eq emailSearch }.toList()
                    .map {
                        NotesDTO(
                            noteId = it[noteId],
                            title = it[title],
                            entry = it[entry],
                            color = it[color],
                            email = it[email],
                            date = it[date]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchAllByTitle(titleSearch: String, emailSearch: String): List<NotesDTO> {
        return try {
            transaction {
                Notes.select { (Notes.email eq emailSearch) and (Notes.title eq titleSearch) }.toList()
                    .map {
                        NotesDTO(
                            noteId = it[noteId],
                            title = it[title],
                            entry = it[entry],
                            color = it[color],
                            email = it[email],
                            date = it[date]
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
            val result = Notes.slice(Notes.noteId.max()).select { Notes.noteId.isNotNull() }.singleOrNull()
            lastId = result?.get(Notes.noteId.max()) as? Int ?: 0
        }

        return lastId
    }
}
