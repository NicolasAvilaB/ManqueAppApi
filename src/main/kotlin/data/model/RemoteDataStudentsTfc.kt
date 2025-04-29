package data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteListStudentsTfc(
    val limit: Int,
    val offset: Long,
    val currentPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val totalCount: Long,
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
