package model.chat

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import model.user.User

@Serializable
data class Message(
    val id: String,
    val sender: User,
    val sentAt: LocalDateTime,
    val text: String,
)
