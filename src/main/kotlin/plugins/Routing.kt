package plugins

import com.manque.app.presentation.routes.studentsTFC
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        studentsTFC()
    }
}
