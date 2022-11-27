package client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import model.chat.Chat
import model.chat.Message
import model.user.Session
import model.user.User

const val BASE_URL = "http://206.81.21.172:38676/api"

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

@Serializable
data class PartialQueryResult<T>(val totalElements: Long, val content: List<T>)

@Serializable
data class PasswordSignIn(var nickname: String?, var password: String?)

suspend fun startSession(passwordSignIn: PasswordSignIn): Session {
    return client.post("$BASE_URL/sessions") {
        contentType(ContentType.Application.Json)
        setBody(passwordSignIn)
    }.body()
}

suspend fun getCurrentUser(session: Session): User {
    return client.get("$BASE_URL/users/me") {
        bearerAuth(session.id)
    }.body()
}

suspend fun getCurrentUserChats(session: Session): PartialQueryResult<Chat> {
    return client.get("$BASE_URL/chats") {
        parameter("maxResults", 100)
        bearerAuth(session.id)
    }.body()
}

suspend fun getChatMessages(session: Session, chat: Chat): PartialQueryResult<Message> {
    return client.get("$BASE_URL/chats/${chat.id}/messages") {
        parameter("maxResults", 100)
        bearerAuth(session.id)
    }.body()
}
