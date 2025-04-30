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
            url = "jdbc:mysql://mtalleres.com:3306/cmt107912_manque?useSSL=false&serverTimezone=UTC",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "cmt107912_manqueuser",
            password = "Manque_2024_*%_Condor_*"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}
