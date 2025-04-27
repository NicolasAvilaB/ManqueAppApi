package data.tablesql

import org.jetbrains.exposed.sql.Table

object TableSqlStudentsTFC : Table("registro_alumno_taller_tsc") {
    val rut = varchar("Rut", 12)
    val name = varchar("Nombre", 70)
    val address = varchar("Direccion", 90)
    val phone = integer("Telefono")
    val email = varchar("Email", 100)
    val grade = varchar("Curso", 30)
    val desHealth = varchar("AntecedentesSalud", 200)
    val person = varchar("Apoderado", 70)
    val taller = varchar("Taller", 100)
    val idTaller = integer("IdTaller")
    val detailGrade = varchar("DetalleCurso", 30)

    override val primaryKey = PrimaryKey(rut)
}
