package com.manque.app.presentation.routes

import com.manque.app.data.model.Constants.MESSAGE_RUT_NOT_FOUND
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_DELETED_SUCCESS
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_INSERTED_SUCCESS
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_NOT_FOUND
import com.manque.app.data.model.Constants.MESSAGE_STUDENT_UPDATED_SUCCESS
import data.DatabaseRepository
import data.model.RemoteDataStudentsTfc
import data.model.RemoteErrorMessage
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.studentsTFC() {
    val databaseRepository = DatabaseRepository()

    get("/studentsTFC") {
        val users = databaseRepository.getAllUsers()
        call.respond(status = HttpStatusCode.OK, users?.listTfc.orEmpty())
    }

    get("/studentsTFC/{rut}") {
        val rut = call.parameters["rut"].toString()
        val student = databaseRepository.searchStudent(rut)
        if (student == null) {
            call.respond(HttpStatusCode.BadRequest, MESSAGE_STUDENT_NOT_FOUND)
        } else {
            call.respond(HttpStatusCode.OK, student)
        }
    }

    post("/studentsTFC") {
        val student = call.receive<RemoteDataStudentsTfc>()
        databaseRepository.insertStudent(student)
        call.respond(HttpStatusCode.Created, MESSAGE_STUDENT_INSERTED_SUCCESS)
    }

    delete("/studentsTFC/{rut}") {
        val rut = call.parameters["rut"].toString()
        if (rut == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_RUT_NOT_FOUND))
        } else {
            val success = databaseRepository.deleteStudent(rut)
            if (success) {
                call.respond(HttpStatusCode.OK, MESSAGE_STUDENT_DELETED_SUCCESS)
            } else {
                call.respond(HttpStatusCode.BadRequest, MESSAGE_STUDENT_NOT_FOUND)
            }
        }
    }

    put("/studentsTFC/{rut}") {
        val rut = call.parameters["rut"].toString()
        if (rut == null) {
            call.respond(HttpStatusCode.BadRequest, RemoteErrorMessage(MESSAGE_RUT_NOT_FOUND))
        } else {
            val updatedStudent = call.receive<RemoteDataStudentsTfc>()
            val success = databaseRepository.updateStudent(rut, updatedStudent)
            if (success) {
                call.respond(HttpStatusCode.OK, MESSAGE_STUDENT_UPDATED_SUCCESS)
            } else {
                call.respond(HttpStatusCode.BadRequest, MESSAGE_STUDENT_NOT_FOUND)
            }
        }
    }
}
