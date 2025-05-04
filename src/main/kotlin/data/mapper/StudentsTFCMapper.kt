package com.manque.app.data.mapper

import data.model.RemoteDataStudentsTfc
import io.r2dbc.spi.Row

internal fun studentsTFCMapperRemoteDataStudent(
    row: Row
): RemoteDataStudentsTfc {
    return RemoteDataStudentsTfc(
        rut = row.get("Rut", String::class.java) ?: "",
        name = row.get("Nombre", String::class.java) ?: "",
        address = row.get("Direccion", String::class.java) ?: "",
        phone = row.get("Telefono", Int::class.java) ?: 0,
        email = row.get("Email", String::class.java) ?: "",
        grade = row.get("Curso", String::class.java) ?: "",
        desHealth = row.get("AntecedentesSalud", String::class.java) ?: "",
        person = row.get("Apoderado", String::class.java) ?: "",
        taller = row.get("Taller", String::class.java) ?: "",
        idTaller = row.get("IdTaller", Int::class.java) ?: 0,
        detailGrade = row.get("DetalleCurso", String::class.java) ?: ""
    )
}
