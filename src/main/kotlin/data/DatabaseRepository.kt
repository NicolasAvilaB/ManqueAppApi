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
import io.r2dbc.spi.Connection
import kotlin.math.ceil
import kotlinx.coroutines.reactive.awaitSingle
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DatabaseRepository {

    suspend fun getAllUsers(
        limit: Int,
        page: Int
    ): RemoteListStudentsTfc = DatabaseFactory.dbQuery { connection ->
        val startConnection = Mono.from(connection.create())

        val totalRecords = getTotalRecords(connection = startConnection).awaitSingle()

        val totalPages = ceil(totalRecords.toDouble() / limit).toInt()
        val adjustedPage = if (page > totalPages) totalPages else page
        val offset = if (adjustedPage > 1) (adjustedPage - 1) * limit else 0

        val studentsList = mutableListOf<RemoteDataStudentsTfc>()

        val results = startConnection.flatMap { conn ->
            Mono.from(
                conn.createStatement("SELECT * FROM registro_alumno_taller_tsc LIMIT $limit OFFSET $offset")
                    .execute()
            ).flatMapMany { result ->
                Flux.from(
                    result.map { row ->
                        RemoteDataStudentsTfc(
                            rut = row.get("Rut", String::class.java) ?: "",
                            name = row.get("Nombre", String::class.java) ?: "",
                            address = row.get("Direccion", String::class.java) ?: "",
                            phone = row.get("Telefono", Int::class.java) ?: 0,
                            email = row.get("Email", String::class.java) ?: "",
                            grade = row.get("Curso", String::class.java) ?: "",
                            desHealth = row.get("AntecedentesSalud", String::class.java) ?: "",
                            person = row.get("Apoderado", String::class.java) ?: "",
                            taller = row.get("Taller", String::class.java) ?: "",
                            idTaller = row.get("IdTaller", Int::class.java) ?: 0,
                            detailGrade = row.get("DetalleCurso", String::class.java) ?: ""
                        )
                    }
                )
            }.collectList()
        }.awaitSingle()
        studentsList.addAll(results)

        val hasNextPage = offset + limit < totalRecords
        val hasPreviousPage = offset > 0

        RemoteListStudentsTfc(
            limit = limit,
            currentPage = adjustedPage,
            hasPreviousPage = hasPreviousPage,
            hasNextPage = hasNextPage,
            totalRecords = totalRecords,
            totalPages = totalPages,
            listTFC = studentsList
        )
    }

    private suspend fun getTotalRecords(connection: Mono<Connection>): Publisher<Long> {
        return connection.flatMap { conn ->
            Mono.from(
                conn.createStatement("SELECT COUNT(rut) FROM registro_alumno_taller_tsc")
                    .execute()
            )
                .flatMap { result ->
                    val count = result.map { row -> row.get(0, Long::class.java) ?: 0L }
                    Mono.just(count)
                }
        }.awaitSingle()
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
