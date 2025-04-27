package data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteErrorMessage(
    val message: String
)