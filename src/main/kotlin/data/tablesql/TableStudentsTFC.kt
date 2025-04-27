package data.tablesql

import org.jetbrains.exposed.sql.Table

object TableStudentsTFC : Table("registro_alumno_taller_tsc") {
    val rut = varchar("Rut", 12).autoIncrement()
    val name = varchar("Nombre", 70)
    val address = varchar("Direccion", 90)
    val phone = integer("Telefono")

    override val primaryKey = PrimaryKey(rut)
}
