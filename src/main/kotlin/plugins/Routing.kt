package plugins

import com.manque.app.presentation.routes.studentsTFC
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        studentsTFC()
    }
}
