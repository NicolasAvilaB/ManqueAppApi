package data

import data.model.DatabaseFactory
import data.model.RemoteDataStudentsTfc
import data.model.UsersTFCWrapper
import data.tablesql.TableSqlStudentsTFC
import data.tablesql.TableSqlStudentsTFC.address
import data.tablesql.TableSqlStudentsTFC.desHealth
import data.tablesql.TableSqlStudentsTFC.detailGrade
import data.tablesql.TableSqlStudentsTFC.email
import data.tablesql.TableSqlStudentsTFC.grade
import data.tablesql.TableSqlStudentsTFC.idTaller
import data.tablesql.TableSqlStudentsTFC.name
import data.tablesql.TableSqlStudentsTFC.person
import data.tablesql.TableSqlStudentsTFC.phone
import data.tablesql.TableSqlStudentsTFC.rut
import data.tablesql.TableSqlStudentsTFC.taller
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseRepository {

    suspend fun getAllUsers(): UsersTFCWrapper? = DatabaseFactory.dbQuery {
        val users = TableSqlStudentsTFC.selectAll().map { toUser(it) }
        if (users.isNotEmpty()) UsersTFCWrapper(users) else null
    }

    suspend fun insertStudent(student: RemoteDataStudentsTfc) = DatabaseFactory.dbQuery {
        TableSqlStudentsTFC.insert {
            it[rut] = student.rut.toString()
            it[name] = student.name.toString()
            it[address] = student.address.toString()
            it[phone] = student.phone ?: 0
            it[email] = student.email.toString()
            it[grade] = student.grade.toString()
            it[desHealth] = student.desHealth.toString()
            it[person] = student.person.toString()
            it[taller] = student.taller.toString()
            it[idTaller] = student.idTaller ?: 0
            it[detailGrade] = student.detailGrade.toString()
        }
    }

    suspend fun deleteStudent(rut: String): Boolean = DatabaseFactory.dbQuery {
        val deletedCount = TableSqlStudentsTFC.deleteWhere { TableSqlStudentsTFC.rut eq rut }
        deletedCount > 0
    }

    suspend fun updateStudent(
        rut: String,
        updatedStudent: RemoteDataStudentsTfc
    ): Boolean = DatabaseFactory.dbQuery {
        val updatedCount = TableSqlStudentsTFC.update({ TableSqlStudentsTFC.rut eq rut }) {
            it[name] = updatedStudent.name.toString()
            it[address] = updatedStudent.address.toString()
            it[phone] = updatedStudent.phone ?: 0
            it[email] = updatedStudent.email.toString()
            it[grade] = updatedStudent.grade.toString()
            it[desHealth] = updatedStudent.desHealth.toString()
            it[person] = updatedStudent.person.toString()
            it[taller] = updatedStudent.taller.toString()
            it[idTaller] = updatedStudent.idTaller ?: 0
            it[detailGrade] = updatedStudent.detailGrade.toString()

        }
        updatedCount > 0
    }

    suspend fun searchStudent(rut: String): RemoteDataStudentsTfc? = DatabaseFactory.dbQuery {
        TableSqlStudentsTFC.selectAll()
            .map { toUser(it) }
            .singleOrNull { it.rut == rut }
    }

    private fun toUser(row: ResultRow): RemoteDataStudentsTfc {
        return RemoteDataStudentsTfc(
            rut = row[rut],
            name = row[name],
            address = row[address],
            phone = row[phone],
            email = row[email],
            grade = row[grade],
            desHealth = row[desHealth],
            person = row[person],
            taller = row[taller],
            idTaller = row[idTaller],
            detailGrade = row[detailGrade],
        )
    }
}
