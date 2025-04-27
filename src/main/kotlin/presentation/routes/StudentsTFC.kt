package com.manque.app.presentation.routes

import data.DatabaseRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.studentsTFC() {
    val databaseRepository = DatabaseRepository()

    get("/studentsTFCAssign") {
        val users = databaseRepository.getAllUsers()
        call.respond(status = HttpStatusCode.OK, users)
    }
}
