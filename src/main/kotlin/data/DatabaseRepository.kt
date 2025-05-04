package data

import com.manque.app.data.mapper.studentsTFCMapperRemoteDataStudent
import data.model.DatabaseFactory
import data.model.RemoteDataStudentsTfc
import data.model.RemoteListStudentsTfc
import io.r2dbc.spi.Connection
import kotlin.math.ceil
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle

class DatabaseRepository {

    suspend fun getAllUsers(
        limit: Int,
        page: Int
    ): RemoteListStudentsTfc {
        val connection = DatabaseFactory.getConnection().awaitSingle()
        try {
            val totalRecords = getTotalRecords(connection)
            val totalPages = ceil(totalRecords.toDouble() / limit).toInt()
            val adjustedPage = if (page > totalPages) totalPages else page
            val offset = if (adjustedPage > 1) (adjustedPage - 1) * limit else 0

            val statement = connection.createStatement(
                "SELECT * FROM registro_alumno_taller_tsc LIMIT $limit OFFSET $offset"
            )

            val result = statement.execute().awaitSingle()
            val rows = result.map { row, _ ->
                studentsTFCMapperRemoteDataStudent(row)
            }.asFlow().toList()

            return RemoteListStudentsTfc(
                limit = limit,
                currentPage = adjustedPage,
                hasPreviousPage = offset > 0,
                hasNextPage = offset + limit < totalRecords,
                totalRecords = totalRecords,
                totalPages = totalPages,
                listTFC = rows
            )
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }

    private suspend fun getTotalRecords(connection: Connection): Long {
        val result = connection
            .createStatement("SELECT COUNT(Rut) FROM registro_alumno_taller_tsc")
            .execute()
            .awaitSingle()

        return result
            .map { row, _ -> row.get(0, Long::class.java) ?: 0L }
            .asFlow()
            .firstOrNull() ?: 0L
    }

    suspend fun searchStudent(
        rut: String
    ): RemoteListStudentsTfc? {
        val connection = DatabaseFactory.getConnection().awaitSingle()
        try {
            val result = connection
                .createStatement("SELECT * FROM registro_alumno_taller_tsc WHERE Rut = ?")
                .bind(0, rut)
                .execute()
                .awaitSingle()

            val students = result.map { row, _ ->
                studentsTFCMapperRemoteDataStudent(row)
            }.asFlow().toList()

            if (students.isEmpty()) return null

            return RemoteListStudentsTfc(
                limit = 1,
                currentPage = 1,
                hasPreviousPage = false,
                hasNextPage = false,
                totalRecords = 1,
                totalPages = 1,
                listTFC = students
            )
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }

    suspend fun insertStudent(student: RemoteDataStudentsTfc): Boolean {
        val connection = DatabaseFactory.getConnection().awaitSingle()
        try {
            val query = """
            INSERT INTO registro_alumno_taller_tsc (Rut, Nombre, Direccion, Telefono, Email, Curso, AntecedentesSalud, Apoderado, Taller, IdTaller, DetalleCurso)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

            val result = connection
                .createStatement(query)
                .bind(0, student.rut.toString())
                .bind(1, student.name.toString())
                .bind(2, student.address.toString())
                .bind(3, student.phone ?: 0)
                .bind(4, student.email.toString())
                .bind(5, student.grade.toString())
                .bind(6, student.desHealth.toString())
                .bind(7, student.person.toString())
                .bind(8, student.taller.toString())
                .bind(9, student.idTaller ?: 0)
                .bind(10, student.detailGrade.toString())
                .execute()
                .awaitSingle()

            return result.rowsUpdated.awaitSingle() > 0
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }

    suspend fun deleteStudent(rut: String): Boolean {
        val connection = DatabaseFactory.getConnection().awaitSingle()
        try {
            val query = "DELETE FROM registro_alumno_taller_tsc WHERE Rut = ?"

            val result = connection
                .createStatement(query)
                .bind(0, rut)
                .execute()
                .awaitSingle()

            return result.rowsUpdated.awaitSingle() > 0
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }

    suspend fun updateStudent(
        rut: String,
        updatedStudent: RemoteDataStudentsTfc
    ): Boolean {
        val connection = DatabaseFactory.getConnection().awaitSingle()
        try {
            val query = """
            UPDATE registro_alumno_taller_tsc 
            SET Nombre = ?, Direccion = ?, Telefono = ?, Email = ?, Curso = ?, AntecedentesSalud = ?, Apoderado = ?, Taller = ?, IdTaller = ?, DetalleCurso = ?
            WHERE Rut = ?
        """

            val result = connection
                .createStatement(query)
                .bind(0, updatedStudent.name.toString())
                .bind(1, updatedStudent.address.toString())
                .bind(2, updatedStudent.phone ?: 0)
                .bind(3, updatedStudent.email.toString())
                .bind(4, updatedStudent.grade.toString())
                .bind(5, updatedStudent.desHealth.toString())
                .bind(6, updatedStudent.person.toString())
                .bind(7, updatedStudent.taller.toString())
                .bind(8, updatedStudent.idTaller ?: 0)
                .bind(9, updatedStudent.detailGrade.toString())
                .bind(10, rut)
                .execute()
                .awaitSingle()

            return result.rowsUpdated.awaitSingle() > 0
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }
}
