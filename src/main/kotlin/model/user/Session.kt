package model.user

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: String,
)
