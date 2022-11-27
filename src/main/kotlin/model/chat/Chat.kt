package model.chat

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Long,
    val invitationCode: String,
    val name: String,
    var selected: Boolean = false,
)
