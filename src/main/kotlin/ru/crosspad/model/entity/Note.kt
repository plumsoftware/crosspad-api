package ru.crosspad.model.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.crosspad.model.dto.NotesDTO

object Note : Table("notes") {
    private val noteId = Note.integer("id")
    private val title = Note.varchar("title", 50)
    private val entry = Note.varchar("entry", 3000)
    private val color = Note.varchar("color", 20)
    private val email = Note.varchar("email", 100)
    private val date = Note.long("date")

    fun insert(notesDTO: NotesDTO) {
        transaction {
            Note.insert {
                it[noteId] = notesDTO.noteId
                it[title] = notesDTO.title
                it[entry] = notesDTO.entry
                it[color] = notesDTO.color
                it[email] = notesDTO.email
                it[date] = notesDTO.date
            }
        }
    }

    fun fetchAllByEmail(emailSearch: String): List<NotesDTO> {
        return try {
            transaction {
                Note.select { email eq emailSearch }.toList()
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
                Note.select { (email eq emailSearch) and (title eq titleSearch) }.toList()
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
            val result = Note.slice(noteId.max()).select { noteId.isNotNull() }.singleOrNull()
            lastId = result?.get(noteId.max()) as? Int ?: 0
        }

        return lastId
    }
}
