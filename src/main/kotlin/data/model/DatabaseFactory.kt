package data.model

import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import reactor.core.publisher.Mono

object DatabaseFactory {
    private lateinit var connectionFactory: ConnectionFactory

    fun init() {
        if (!::connectionFactory.isInitialized) {
            connectionFactory = ConnectionFactories.get(
                "r2dbc:mysql://cmt107912_manqueuser:Manque_2024_*%25_Condor_*@mtalleres.com:3306/cmt107912_manque?useSSL=false&serverTimezone=UTC"
            )
        }
    }

    fun getConnection(): Mono<Connection> {
        return Mono.from(connectionFactory.create())
    }
}
