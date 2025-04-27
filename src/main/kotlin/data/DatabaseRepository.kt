package data

import data.model.DatabaseFactory
import data.model.RemoteDataStudentsTfc
import data.tablesql.TableStudentsTFC
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class DatabaseRepository {

    suspend fun getAllUsers(): List<RemoteDataStudentsTfc> = DatabaseFactory.dbQuery {
        TableStudentsTFC.selectAll().map { toUser(it) }
    }

    private fun toUser(row: ResultRow): RemoteDataStudentsTfc {
        return RemoteDataStudentsTfc(
            rut = row[TableStudentsTFC.rut],
            name = row[TableStudentsTFC.name],
            address = row[TableStudentsTFC.address],
            phone = row[TableStudentsTFC.phone]
        )
    }
}
