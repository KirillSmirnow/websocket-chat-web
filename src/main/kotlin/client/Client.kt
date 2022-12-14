package client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import model.chat.Chat
import model.chat.Message
import model.user.Session
import model.user.User

const val BASE_URL = "https://comunicazione.art/api"

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    expectSuccess = true
    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            when (cause) {
                is ClientRequestException -> {
                    val errors: List<Error> = cause.response.body()
                    val message = errors.groupBy({ it.field }, { it.message })
                        .mapValues { it.value.joinToString() }
                        .entries
                        .joinToString("\n") { if (it.key != null) "${it.key}: ${it.value}" else it.value }
                    window.alert(message)
                }

                is ServerResponseException -> {
                    val responseContent = cause.response.bodyAsText()
                    window.alert(responseContent)
                }
            }
        }
    }
}

@Serializable
data class Error(val message: String, val field: String?)

@Serializable
data class PartialQueryResult<T>(val totalElements: Long, val content: List<T>)

@Serializable
data class UserRegistration(
    var nickname: String?,
    var password: String?,
    @Transient var passwordConfirmation: String? = null,
    var name: String?,
) {
    fun isPasswordConfirmed(): Boolean {
        return password == passwordConfirmation
    }
}

suspend fun registerUser(userRegistration: UserRegistration): Session {
    return client.post("$BASE_URL/users") {
        contentType(ContentType.Application.Json)
        setBody(userRegistration)
    }.body()
}

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

@Serializable
data class ChatCreation(var name: String?)

suspend fun createChat(session: Session, chatCreation: ChatCreation): Chat {
    return client.post("$BASE_URL/chats") {
        bearerAuth(session.id)
        contentType(ContentType.Application.Json)
        setBody(chatCreation)
    }.body()
}

@Serializable
data class ChatJoining(var invitationCode: String?)

suspend fun joinChat(session: Session, chatJoining: ChatJoining): Chat {
    return client.put("$BASE_URL/chats/joining") {
        bearerAuth(session.id)
        contentType(ContentType.Application.Json)
        setBody(chatJoining)
    }.body()
}

suspend fun getChatMembers(session: Session, chat: Chat): PartialQueryResult<User> {
    return client.get("$BASE_URL/chats/${chat.id}/members") {
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

@Serializable
data class MessageSending(var text: String?)

suspend fun sendMessage(session: Session, chat: Chat, messageSending: MessageSending): Message {
    return client.post("$BASE_URL/chats/${chat.id}/messages") {
        bearerAuth(session.id)
        contentType(ContentType.Application.Json)
        setBody(messageSending)
    }.body()
}
