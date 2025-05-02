package data.model

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseFactory {
    private lateinit var connectionFactory: ConnectionFactory

    fun init() {
        if (!::connectionFactory.isInitialized) {
            connectionFactory = ConnectionFactories
                .get("r2dbc:mysql://" +
                        "cmt107912_manqueuser:" +
                        "Manque_2024_*%_Condor_*@mtalleres.com:3306/" +
                        "cmt107912_manque?useSSL=false&serverTimezone=UTC")
        }
    }

    suspend fun <T> dbQuery(block: suspend (connectionFactory: ConnectionFactory) -> T): T {
        return withContext(Dispatchers.IO) {
            block(connectionFactory)
        }
    }
}
