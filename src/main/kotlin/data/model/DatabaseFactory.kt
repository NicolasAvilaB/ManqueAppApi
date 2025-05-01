package data.model

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:mysql://127.0.0.1:3306/nombre_base_de_datos?useSSL=false&serverTimezone=UTC",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "usuario",
            password = "password"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}
