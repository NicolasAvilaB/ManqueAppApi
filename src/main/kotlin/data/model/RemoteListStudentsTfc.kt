package data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteListStudentsTfc(
    val limit: Int,
    val currentPage: Int,
    val hasPreviousPage: Boolean,
    val hasNextPage: Boolean,
    val totalRecords: Long,
    val totalPages: Int,
    val listTFC: List<RemoteDataStudentsTfc>,
)

@Serializable
data class RemoteDataStudentsTfc(
    val rut: String?,
    val name: String?,
    val address: String?,
    val phone: Int?,
    val email: String?,
    val grade: String?,
    val desHealth: String?,
    val person: String?,
    val taller: String?,
    val idTaller: Int?,
    val detailGrade: String?
)
