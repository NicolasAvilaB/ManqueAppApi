package data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteDataStudentsTfc(
    val rut: String,
    val name: String,
    val address: String,
    val phone: Int
)
