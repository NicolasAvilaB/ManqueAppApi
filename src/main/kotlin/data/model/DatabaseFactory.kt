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
                "r2dbc:mysql://usuario basedatos:password@dominio:3306/base_de_datos?useSSL=false&serverTimezone=UTC"
            )
        }
    }

    fun getConnection(): Mono<Connection> {
        return Mono.from(connectionFactory.create())
    }
}
