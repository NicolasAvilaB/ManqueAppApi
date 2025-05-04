package com.manque.app.presentation.routes

import com.manque.app.data.model.Constants.MESSAGE_ALL_RECORDS_NOT_FOUND
import com.manque.app.data.model.Constants.MESSAGE_ERROR_INSERTED
import com.manque.app.data.model.Constants.MESSAGE_NOT_INSERTED_SUCCESS
import com.manque.app.data.model.Constants.MESSAGE_RUT_NOT_FOUND
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_DELETED_SUCCESS
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_INSERTED_SUCCESS
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_NOT_FOUND
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_UPDATED_SUCCESS
import data.DatabaseRepository
import data.model.RemoteDataStudentsTfc
import data.model.RemoteErrorMessage
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.post
import io.ktor.server.routing.delete
import io.ktor.server.routing.Route

fun Route.studentsTFC() {
    val databaseRepository = DatabaseRepository()

    get("/studentsTFC") {
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
        val page = call.request.queryParameters["page"]?.toIntOrNull()?.takeIf { it > 0 } ?: 1
        val users = databaseRepository.getAllUsers(
            limit = limit,
            page = page
        )
        if (users == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_ALL_RECORDS_NOT_FOUND))
        } else {
            call.respond(status = HttpStatusCode.OK, users)
        }
    }

    get("/studentsTFC/{rut}") {
        val rut = call.parameters["rut"].toString()
        val student = databaseRepository.searchStudent(rut)
        if (student == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_STUDENT_NOT_FOUND))
        } else {
            call.respond(HttpStatusCode.OK, student)
        }
    }

    post("/studentsTFC") {
        val student = call.receive<RemoteDataStudentsTfc>()
        if (student == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_NOT_INSERTED_SUCCESS))
        } else {
            val insert = databaseRepository.insertStudent(student)
            if (insert) {
                call.respond(HttpStatusCode.Created, RemoteErrorMessage(MESSAGE_STUDENT_INSERTED_SUCCESS))
            } else {
                call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_ERROR_INSERTED))
            }
        }
    }

    delete("/studentsTFC/{rut}") {
        val rut = call.parameters["rut"].toString()
        if (rut == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_RUT_NOT_FOUND))
        } else {
            val delete = databaseRepository.deleteStudent(rut)
            if (delete) {
                call.respond(HttpStatusCode.OK, RemoteErrorMessage(MESSAGE_STUDENT_DELETED_SUCCESS))
            } else {
                call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_STUDENT_NOT_FOUND))
            }
        }
    }

    put("/studentsTFC/{rut}") {
        val rut = call.parameters["rut"].toString()
        if (rut == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_RUT_NOT_FOUND))
        } else {
            val updatedStudent = call.receive<RemoteDataStudentsTfc>()
            val update = databaseRepository.updateStudent(rut, updatedStudent)
            if (update) {
                call.respond(HttpStatusCode.OK, RemoteErrorMessage(MESSAGE_STUDENT_UPDATED_SUCCESS))
            } else {
                call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_STUDENT_NOT_FOUND))
            }
        }
    }
}
