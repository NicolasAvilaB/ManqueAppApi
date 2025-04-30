package data

import data.model.DatabaseFactory
import data.model.RemoteDataStudentsTfc
import data.model.RemoteListStudentsTfc
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
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlin.math.ceil

class DatabaseRepository {

    private var studentsList: MutableList<RemoteDataStudentsTfc> = mutableListOf()
    private var lastKnownTotalRecords: Int = 0

    suspend fun getAllUsers(
        limit: Int,
        page: Int
    ): RemoteListStudentsTfc = DatabaseFactory.dbQuery {
        val totalRecords = TableSqlStudentsTFC.select(rut).count()

        if (totalRecords.toInt() != lastKnownTotalRecords || studentsList.isEmpty()) {
            studentsList.clear()
            val offset = if (page > 1) (page - 1) * limit else 0
            TableSqlStudentsTFC
                .selectAll()
                .limit(count = limit)
                .offset(start = offset.toLong())
                .forEach { row ->
                    val student = RemoteDataStudentsTfc(
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
                        detailGrade = row[detailGrade]
                    )
                    studentsList.add(student)
                }
            lastKnownTotalRecords = totalRecords.toInt()
        }

        val offset = if (page > 1) (page - 1) * limit else 0
        val paginatedUsers = studentsList.subList(offset, (offset + limit).coerceAtMost(studentsList.size))

        val hasNextPage = offset + limit < totalRecords
        val hasPreviousPage = offset > 0
        val totalPages = ceil(totalRecords.toDouble() / limit).toInt()

        RemoteListStudentsTfc(
            limit = limit,
            currentPage = page,
            hasPreviousPage = hasPreviousPage,
            hasNextPage = hasNextPage,
            totalRecords = totalRecords,
            totalPages = totalPages,
            listTFC = paginatedUsers
        )
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

    suspend fun searchStudent(rut: String): RemoteListStudentsTfc? = DatabaseFactory.dbQuery {
        TableSqlStudentsTFC
            .selectAll()
            .where { TableSqlStudentsTFC.rut eq rut }
            .withDistinct()
            .map { toUser(it) }
            .singleOrNull()
    }

    private fun toUser(row: ResultRow): RemoteListStudentsTfc {
        return RemoteListStudentsTfc(
            limit = 1,
            currentPage = 1,
            hasPreviousPage = false,
            hasNextPage = false,
            totalRecords = 1,
            totalPages = 1,
            listTFC = listOf(
                RemoteDataStudentsTfc(
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
                    detailGrade = row[detailGrade]
                )
            )
        )
    }
}
