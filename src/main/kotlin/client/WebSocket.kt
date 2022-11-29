package client

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import model.chat.Message
import model.user.Session

class WebSocket {

    private val wsBaseUrl = BASE_URL.replace("http", "ws")
    private val client: HttpClient = createClient()
    private var session: Session? = null

    private fun createClient(): HttpClient {
        return HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }

    suspend fun subscribe(session: Session, messageListener: (Message) -> Unit) {
        if (this.session == session) return
        this.session = session
        client.webSocket("$wsBaseUrl/messages") {
            send(session.id)
            while (true) {
                val message = receiveDeserialized<Message>()
                if (this@WebSocket.session == session) {
                    messageListener(message)
                } else {
                    break
                }
            }
        }
    }

    fun unsubscribe() {
        session = null
    }
}
